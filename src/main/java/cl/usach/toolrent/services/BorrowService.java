package cl.usach.toolrent.services;

import cl.usach.toolrent.entities.*;
import cl.usach.toolrent.repositories.BorrowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class BorrowService {
    @Autowired
    private BorrowRepository borrowRepository;

    //Llamar a servicios
    @Autowired
    private ClientService clientService;
    @Autowired
    private ToolService toolService;
    @Autowired
    private UserService userService;
    @Autowired
    private FineService fineService;
    @Autowired
    private MoveService moveService;

    public BorrowEntity getBorrowById(Long id){
        return borrowRepository.findBorrowById(id);
    }

    public int calculateAmount(ArrayList<Long> toolIds, Date borrowDate, Date returnDate){
        int days = fineService.calculateDays(borrowDate, returnDate);
        int amount = 0;
        for (Long toolId : toolIds){
            amount = amount + (toolService.getToolById(toolId).getDailyTariff());
        }
        return amount;
    }

    public BorrowEntity createBorrow(Long clientId, ArrayList<Long> toolIds, Long userId, Date returnDate) {

        ClientEntity client = clientService.getClientById(clientId);
        UserEntity user = userService.getUserById(userId);

        ArrayList<ToolEntity> tools = new ArrayList<>();
        for(Long toolId : toolIds){
            ToolEntity tool = toolService.getToolById(toolId);
            tools.add(tool);
        }

        //Criterio, el cliente no debe tener atrasos
        if(!client.getExpiredBorrows().isEmpty()){
            throw new RuntimeException("The client has expired borrows");
        }

        //Criterio, el cliente no debe estar restringido
        if(client.getState().equals(ClientEntity.ClientState.Restricted)){
            throw new RuntimeException("The client is restricted");
        }

        //Criterio, el cliente no debe tener mas de 5 presatamos activos
        if(client.getBorrowedTools().size() >= 5){
            throw new RuntimeException("The client has more than 5 active borrowed tools");
        }

        //Criterio, las herramientas estan disponibles
        for(Long toolId : toolIds){
            if(toolService.getToolById(toolId).getState().equals(ToolEntity.ToolState.Available)){
                throw new RuntimeException("The selected tool is not available");
            }
        }

        //Criterio, la herramienta debe arrendarse mas de 1 dia
        if(fineService.calculateDays(Date.valueOf(LocalDate.now()), returnDate) < 1){
            throw new RuntimeException("Borrows must be more than 1 day long");
        }


        MoveEntity move = moveService.createMove();
        move.setDate(Date.valueOf(LocalDate.now()));
        move.setType(MoveEntity.MovementType.Borrow);
        move.setResponsible(userService.getUserById(userId));
        move.setQuantityAffected(tools.size());
        moveService.saveMove(move);


        BorrowEntity borrow = new BorrowEntity();
        borrow.setBorrowedTools(tools);
        borrow.setCost(calculateAmount(toolIds, returnDate, Date.valueOf(LocalDate.now())));
        borrow.setBorrowDate(Date.valueOf(LocalDate.now())); //No estoy seguro como funciona esto
        borrow.setReturnDate(returnDate);
        borrow.setResponsible(user);
        borrow.setClient(client);
        borrow.setBorrowState(BorrowEntity.BorrowState.Active);
        return borrowRepository.save(borrow);
    }

    public void returnBorrow(Long borrowId, Date returnDate, Long userId) {
        BorrowEntity borrow = borrowRepository.findBorrowById(borrowId);
        ArrayList<ToolEntity> borrowedTools = borrow.getBorrowedTools();

        float damageAmount = 0;
        int delayAmount = 0;
        boolean damaged = false;
        for (ToolEntity tool : borrowedTools){
            //calcular daño
            switch (tool.getDamageLevel()){
                case NoDamage:
                    tool.setDamageLevel(ToolEntity.DamageLevel.NoDamage);
                    break;
                case SmallDamage:
                    damageAmount = damageAmount + tool.getReplacementValue() * 0.2f;
                    tool.setDamageLevel(ToolEntity.DamageLevel.SmallDamage);
                    damaged = true;
                    break;
                case MediumDamage:
                    damageAmount = damageAmount + tool.getReplacementValue() * 0.5f;
                    tool.setDamageLevel(ToolEntity.DamageLevel.MediumDamage);
                    damaged = true;
                    break;
                case HighDamage:
                    damageAmount = damageAmount + tool.getReplacementValue() * 0.8f;
                    tool.setDamageLevel(ToolEntity.DamageLevel.HighDamage);
                    damaged = true;
                    break;
                case Unusable:
                    tool.setDamageLevel(ToolEntity.DamageLevel.Unusable);
                    damageAmount = damageAmount + tool.getReplacementValue();
                    damaged = true;
                    break;
            }
            //calcular tiempo
            int delayDays = fineService.calculateDays(borrow.getReturnDate(), returnDate);
            delayAmount = delayAmount + (tool.getDailyTariff() * delayDays);


        }

        if(delayAmount == 0){
            if(damaged){ //sin atraso con daño
                FineEntity fine = new FineEntity();
                fine.setDelayDays(fineService.calculateDays(borrow.getReturnDate(), returnDate));
                fine.setAmount(damageAmount);
                fine.setType(FineEntity.FineType.Damage);
                fine.setClient(borrow.getClient());
            }else{       //sin atraso sin daño

            }
        }else{
            if(damaged){ //con atraso con daño
                FineEntity fine = new FineEntity();
                fine.setDelayDays(fineService.calculateDays(borrow.getReturnDate(), returnDate));
                fine.setAmount(damageAmount + delayAmount);
                fine.setType(FineEntity.FineType.DelayAndDamage);
                fine.setClient(borrow.getClient());
            }else{       //con atraso sin daño
                FineEntity fine = new FineEntity();
                fine.setDelayDays(fineService.calculateDays(borrow.getReturnDate(), returnDate));
                fine.setAmount(delayAmount);
                fine.setType(FineEntity.FineType.Delay);
                fine.setClient(borrow.getClient());
            }
        }




        MoveEntity move = moveService.createMove();
        move.setDate(Date.valueOf(LocalDate.now()));
        move.setType(MoveEntity.MovementType.Return);
        move.setResponsible(userService.getUserById(userId));
        move.setQuantityAffected(borrowedTools.size());
        moveService.saveMove(move);

        borrowRepository.findBorrowById(borrowId).setBorrowState(BorrowEntity.BorrowState.Returned);
        borrowRepository.save(borrowRepository.findBorrowById(borrowId));
    }

    public ArrayList<BorrowEntity> getActiveBorrows(){
        return (ArrayList<BorrowEntity>) borrowRepository.findByBorrowState(BorrowEntity.BorrowState.Active);
    }

    public ArrayList<BorrowEntity> getOverdueBorrows(){
        return (ArrayList<BorrowEntity>) borrowRepository.findByBorrowState(BorrowEntity.BorrowState.Overdue);
    }

    public ArrayList<BorrowEntity> getBorrowsByClientId(Long clientId){
        return (ArrayList<BorrowEntity>) borrowRepository.findByClient(clientService.getClientById(clientId));
    }


}
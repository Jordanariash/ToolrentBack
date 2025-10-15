
package cl.usach.toolrent.services;
import cl.usach.toolrent.entities.*;
import cl.usach.toolrent.repositories.BorrowRepository;
import cl.usach.toolrent.repositories.FineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private FineRepository fineRepository;

    public BorrowEntity getBorrowById(Long id){
        return borrowRepository.findBorrowById(id);
    }

    public int calculateAmount(List<Long> toolIds, Date borrowDate, Date returnDate){
        int days = fineService.calculateDays(borrowDate, returnDate);
        int amount = 0;
        for (Long toolId : toolIds){
            amount = amount + ((toolService.getToolById(toolId).getDailyTariff())*days);
        }
        return amount;
    }

    public BorrowEntity createBorrow(Long clientId, List<Long> toolIds, Long userId, Date returnDate) {

        ClientEntity client = clientService.getClientById(clientId);
        UserEntity user = userService.getUserById(userId);

        List<ToolEntity> tools = new ArrayList<>();
        for(Long toolId : toolIds){
            ToolEntity tool = toolService.getToolById(toolId);
            tools.add(tool);
        }

        //Criterio, el cliente no debe tener atrasos
        if(clientService.hasExpiredBorrows(clientId)){
            throw new RuntimeException("The client has expired borrows");
        }

        //Criterio, el cliente no debe tener multas
        if(clientService.hasUnpaidFines(clientId)){
            throw new RuntimeException("The client has unpaid fines");
        }

        //Criterio, el cliente no debe estar restringido
        if(client.getState().equals(ClientEntity.ClientState.Restricted)){
            throw new RuntimeException("The client is restricted");
        }

        //Criterio, el cliente no debe tener mas de 5 presatamos activos
        if(client.getBorrows().size() >= 5){
            throw new RuntimeException("The client has more than 5 active borrows");
        }

        //Criterio, las herramientas estan disponibles
        for(Long toolId : toolIds){
            if(!toolService.getToolById(toolId).getState().equals(ToolEntity.ToolState.Available)){
                throw new RuntimeException("The selected tool is not available");
            }
        }

        //Criterio, solo 1 tipo de herramienta por prestamo
        for(int i = 0; i < toolIds.size(); i++){
            for(int j = 0; j < toolIds.size(); j++){
                if(i != j){
                    //Category, pero tambien tener en cuenta el tipo
                    if(toolService.getToolById(toolIds.get(i)).equals(toolService.getToolById(toolIds.get(j)))) {
                        throw new RuntimeException("No more than 1 type of tool by borrow");
                    }
                }
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
        borrow.setCost(calculateAmount(toolIds, Date.valueOf(LocalDate.now()), returnDate));
        borrow.setBorrowDate(Date.valueOf(LocalDate.now())); //No estoy seguro como funciona esto
        borrow.setReturnDate(returnDate);
        borrow.setResponsible(user);
        borrow.setClient(client);
        borrow.setBorrowState(BorrowEntity.BorrowState.Active);
        return borrowRepository.save(borrow);
    }

    public void returnBorrow(Long borrowId, Date returnDate, Long userId, ArrayList<ToolEntity.DamageLevel>  damages ) {
        BorrowEntity borrow = borrowRepository.findBorrowById(borrowId);
        List<ToolEntity> borrowedTools = borrow.getBorrowedTools();

        float damageAmount = 0;
        int delayAmount = 0;
        boolean damaged = false;
        for (int i = 0; i<borrowedTools.size(); i++){//ToolEntity tool : borrowedTools
            //calcular daño
            switch (damages.get(i)){
                case NoDamage:
                    borrowedTools.get(i).setDamageLevel(ToolEntity.DamageLevel.NoDamage);
                    borrowedTools.get(i).setState(ToolEntity.ToolState.Available);
                    break;
                case SmallDamage:
                    damageAmount = damageAmount + borrowedTools.get(i).getReplacementValue() * 0.2f;
                    borrowedTools.get(i).setDamageLevel(ToolEntity.DamageLevel.SmallDamage);
                    borrowedTools.get(i).setState(ToolEntity.ToolState.InRepair);
                    damaged = true;
                    break;
                case MediumDamage:
                    damageAmount = damageAmount + borrowedTools.get(i).getReplacementValue() * 0.5f;
                    borrowedTools.get(i).setDamageLevel(ToolEntity.DamageLevel.MediumDamage);
                    borrowedTools.get(i).setState(ToolEntity.ToolState.InRepair);
                    damaged = true;
                    break;
                case HighDamage:
                    damageAmount = damageAmount + borrowedTools.get(i).getReplacementValue() * 0.8f;
                    borrowedTools.get(i).setDamageLevel(ToolEntity.DamageLevel.HighDamage);
                    borrowedTools.get(i).setState(ToolEntity.ToolState.InRepair);
                    damaged = true;
                    break;
                case Unusable:
                    borrowedTools.get(i).setDamageLevel(ToolEntity.DamageLevel.Unusable);
                    borrowedTools.get(i).setState(ToolEntity.ToolState.OutOfService);
                    damageAmount = damageAmount + borrowedTools.get(i).getReplacementValue();
                    damaged = true;
                    break;
            }
            //calcular tiempo
            int delayDays = fineService.calculateDays(borrow.getReturnDate(), returnDate);
            delayAmount = delayAmount + (borrowedTools.get(i).getDailyTariff() * delayDays);


        }

        if(delayAmount == 0){
            if(damaged){ //sin atraso con daño
                FineEntity fine = new FineEntity();
                fine.setDelayDays(fineService.calculateDays(borrow.getReturnDate(), returnDate));
                fine.setAmount(damageAmount);
                fine.setType(FineEntity.FineType.Damage);
                fine.setClient(borrow.getClient());
                fineRepository.save(fine);
            }else{       //sin atraso sin daño

            }
        }else{
            if(damaged){ //con atraso con daño
                FineEntity fine = new FineEntity();
                fine.setDelayDays(fineService.calculateDays(borrow.getReturnDate(), returnDate));
                fine.setAmount(damageAmount + delayAmount);
                fine.setType(FineEntity.FineType.DelayAndDamage);
                fine.setClient(borrow.getClient());
                fineRepository.save(fine);
            }else{       //con atraso sin daño
                FineEntity fine = new FineEntity();
                fine.setDelayDays(fineService.calculateDays(borrow.getReturnDate(), returnDate));
                fine.setAmount(delayAmount);
                fine.setType(FineEntity.FineType.Delay);
                fine.setClient(borrow.getClient());
                fineRepository.save(fine);
            }
        }

        MoveEntity move = moveService.createMove();
        move.setDate(Date.valueOf(LocalDate.now()));
        move.setType(MoveEntity.MovementType.Return);
        move.setResponsible(userService.getUserById(userId));
        move.setQuantityAffected(borrowedTools.size());
        moveService.saveMove(move);

        borrow.setBorrowState(BorrowEntity.BorrowState.Returned);
        borrowRepository.save(borrow);

    }

    public List<BorrowEntity> getActiveBorrows(){
        return borrowRepository.findByBorrowState(BorrowEntity.BorrowState.Active);
    }

    public List<BorrowEntity> getOverdueBorrows(){
        return borrowRepository.findByBorrowState(BorrowEntity.BorrowState.Overdue);
    }

    public ArrayList<BorrowEntity> getBorrowsByClientId(Long clientId){
        return (ArrayList<BorrowEntity>) borrowRepository.findByClient(clientService.getClientById(clientId));
    }

    public Map<ToolEntity, Integer> mostBorrowedTools(){
        Map<ToolEntity, Integer> counter = new HashMap<>();
        for(BorrowEntity borrow : borrowRepository.findAll()){
            for(ToolEntity tool : borrow.getBorrowedTools()){
                ToolCategory Category = tool.getCategory();
                counter.put(tool, counter.getOrDefault(tool, 0) + 1);
            }
        }
        return counter;
    }


}
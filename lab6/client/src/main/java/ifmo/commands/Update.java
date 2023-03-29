package ifmo.commands;

import ifmo.data.Person;
import ifmo.exceptions.ElementAmountException;
import ifmo.utils.CollectionHandler;
import ifmo.utils.IOHandler;
import ifmo.utils.PersonCreator;

import java.time.LocalDate;
/**
 * Класс отвечающий за команду update {id}
 */
public class Update extends AbstractCommand {
    
    private PersonCreator personCreator;
    private CollectionHandler collectionHandler;

    public Update(PersonCreator personCreator, CollectionHandler collectionHandler){
        super("update", "обновить значение элемента по id");
        this.personCreator = personCreator;
        this.collectionHandler = collectionHandler;
    }
    
    @Override
    public boolean argCheck(String arg){
        try{
            if(arg.equals("placeholderArg")) throw new ElementAmountException();
            Integer.parseInt(arg);
            
            return true;
        } catch (ElementAmountException e) {
            IOHandler.println("Некорректное кол-во аргументов");
        } catch (NumberFormatException ex) {
            IOHandler.println("Введите число");
        }
        return false;
    }

    @Override
    public void execute(String arg){
        if(argCheck(arg)){
            for(Person person :  collectionHandler.getCollection()){
                if(person.getId()==(Integer.parseInt(arg))){
                    collectionHandler.removePerson(person);
                    collectionHandler.addPerson(new Person(Integer.parseInt(arg), personCreator.nameCreate(), personCreator.coordinatesCreate(), LocalDate.now(), personCreator.heightCreate(), personCreator.bdayCreate(), personCreator.eyeColorChoose(), personCreator.hairColorChoose(), personCreator.locationCreate()));
                }
            }
        }
    }
}
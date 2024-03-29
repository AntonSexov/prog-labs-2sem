package ifmo.commands;
import java.io.PrintWriter;
import java.io.StringWriter;

import ifmo.exceptions.ElementAmountException;
import ifmo.requests.Request;
import ifmo.utils.IOHandler;
import ifmo.utils.CollectionHandler;
/**
 * Класс отвечающий за команду count_less_than_height
 */
public class CountLessThanHeight extends AbstractCommand{

    private CollectionHandler collectionHandler;

    public CountLessThanHeight(CollectionHandler collectionHandler) {
        super("count_less_than_height", "выводит количество элементов, значение поля height которых меньше заданного");
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
    public String execute(Request request) {
        if (argCheck(request.getArguments())) {
            int count = (int) collectionHandler.getCollection().stream()
                .filter(person -> person.getHeight() < Integer.parseInt(request.getArguments()))
                .count();
    
            StringWriter sw = new StringWriter();
            PrintWriter output = new PrintWriter(sw, true);
            output.println("Количество элементов с значением height меньше введенного: " + count);
    
            return sw.toString();
        } else {
            return "";
        }
    }
}
package ifmo.commands;
import ifmo.exceptions.ElementAmountException;
import ifmo.utils.IOHandler;
import ifmo.requests.Request;
import ifmo.utils.CollectionHandler;
/**
 * Класс отвечающий за команду clear
 */
public class Clear extends AbstractCommand{

    private CollectionHandler collectionHandler;

    public Clear(CollectionHandler collectionHandler) {
        super("clear", "очистить коллекцию");
        this.collectionHandler = collectionHandler;
    }
    
    @Override
    public boolean argCheck(String arg){
        try{
            if(!arg.equals("placeholderArg")) throw new ElementAmountException();
            return true;
        } catch (ElementAmountException e) {
            IOHandler.println("Некорректное кол-во аргументов");
        } 
        return false;
    }
    /**
     * Очистка коллекции
     * @see ifmo.utils.CollectionHandler#clear()
     * @see java.util.Collections
     */
    @Override
    public String execute(Request request){
        if(argCheck(request.getArguments())){
            collectionHandler.clear();
        }
        return "";
    }
}
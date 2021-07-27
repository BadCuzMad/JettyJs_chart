import java.util.*;

public class Constants {

    public static final int MAX_INTEGER = 0xFFFFFFFF;
    public static final String ERROR = "PROCESS_ERROR";
    public static final String SUCCESS = "PROCESS_SUCCESS";
    public static final String DATA_DIRECTORY = "_data_files"; // папка для хранения данных
    public static final int MODELLING_SIZE = 1000;
    public static final int MODELLING_FTAKT = 1000000;

    public static enum MODELS {
        model1("Старый парк (1000)"), model2 ("Полярно-позиционное кодирование"), model3("Модель ИЗ-2691384(3 ПСП)");
        private String Name;
        private MODELS (String name) { this.Name = name; }
        public String getName() { return Name; }
    }

    public static enum ANNOTATIONS {
        annt1("Length SP = Length IP"), annt2("Полярно-позиционное кодирование"),annt3("ИЗ-2691384(3 ПСП)");
        private String annt;
        private ANNOTATIONS(String s) { this.annt = s; }
        public String getAnnt() {return annt;}
    }

    public static int MODELS_COUNT = MODELS.values().length;

    public static enum TWindowFunction {wfHamming, wfBlackman};
    public static enum TSigMode {smZeroF, smHighF, smMidF};
}

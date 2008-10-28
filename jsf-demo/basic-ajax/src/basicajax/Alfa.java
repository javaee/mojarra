package basicajax;

import javax.faces.event.ActionEvent;
import javax.el.ValueExpression;
import java.util.TreeMap;
import java.util.Collection;
import java.util.Random;

public class Alfa {

    // The list of the NATO Phonetic Alphabet
    String[] alfa = {"alfa", "bravo", "charlie", "delta", "echo", "foxtrot", "golf", "hotel",
            "india", "juliet", "kilo", "lima", "mike", "november", "oscar", "papa", "quebec",
            "romeo", "sierra", "tango", "uniform", "victor", "whiskey", "xray", "yankee", "zulu"};

    // Map to hold values
    TreeMap<String, String> alfaMap= new TreeMap();

    public Alfa() {
        //initialize map
        char ch = 'a';
        int i = 0;
        do {
            Character c = new Character(ch);
            alfaMap.put(c.toString(),alfa[i]);
            ch++;
            i++;
        } while (ch <= 'z');
    }

    public String translate(String alfa) {
         return alfaMap.get(alfa);
    }

    public void process(ActionEvent ae) {
        // ValueExpression ve = ae.getComponent().getValueExpression("str");
        // ve.getValue();
    }

    public Collection getList() {
        return alfaMap.values();
    }

}

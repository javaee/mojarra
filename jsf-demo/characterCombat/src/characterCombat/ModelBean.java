package characterCombat;

import javax.faces.model.SelectItem;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class ModelBean {

    public ModelBean() {
        //create List of Map instances for pre-defined characters
        populate();
    }


    List dataTable = null;

    public List getDataTable() {
System.out.println("getDataTable...");
        //TO DO: this method needs to be intelligent enough to omit
        //the first pick and second pick from the tables.


        //TO DO: does this make sense?
        //needs to return a list of SelectItem instances
/*
        List selectItemList = new ArrayList();
        Iterator iter = dataTable.iterator();
        SelectItem selectItem = null;

        while(iter.hasNext()) {
            CharacterBean item = (CharacterBean) iter.next();
            selectItem = new SelectItem(item.getName());
            selectItemList.add(selectItem);
        }

        return selectItemList;
*/
        return dataTable;
    }

    public void setDataTable(List dataTable) {
        this.dataTable = dataTable;
    }
    

    String firstPick = null;

    public String getFirstPick() {
        return firstPick;
    }

    public void setFirstPick(String firstPick) {
        this.firstPick = firstPick;
    }


    String secondPick = null;

    public String getSecondPick() {
        return secondPick;
    }

    public void setSecondPick(String secondPick) {
        this.secondPick = secondPick;
    }


    private void populate() {
System.out.println("POPULATING...");
         dataTable = new ArrayList();

         CharacterBean item = new CharacterBean();
         item.setName("Gandalf");
         item.setSpecies("Istari");
         item.setLanguage("Common Speech");
         item.setImmortal(true);
         dataTable.add(item);

         item = new CharacterBean();
         item.setName("Frodo");
         item.setSpecies("Hobbit");
         item.setLanguage("Common Speech");
         item.setImmortal(false);
         dataTable.add(item);

         item = new CharacterBean();
         item.setName("Legolas");
         item.setSpecies("Elf");
         item.setLanguage("Quenya/Sindarin");
         item.setImmortal(false);
         dataTable.add(item);
    }
}

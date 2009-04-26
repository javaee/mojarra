package jsf2.demo.scrum.web.controller;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Dr. Spock (spock at dev.java.net)
 */
@ManagedBean(name = "skinManager")
@ViewScoped
public class SkinManager extends AbstractManager implements Serializable {
    private String selectedSkin = "appSelectedColor.css";
    private static final long serialVersionUID = 1L;
    // TODO: complete the implementation

      /**
     * @return the selectedSkin
     */
    public String getSelectedSkin() {
        return selectedSkin;
    }

    /**
     * @param selectedSkin the selectedSkin to set
     */
    public void setSelectedSkin(String selectedSkin) {
        this.selectedSkin = selectedSkin;
    }

    public String orangeSkin(){
        System.out.println("skinManager orangeSkin ");
        setSelectedSkin("appOrangeSkin.css");
        return null;
    }
    public String yellowSkin(){
        System.out.println("skinManager yellowSkin ");
        setSelectedSkin("appYellowSkin.css");
        return null;
    }
    public String redSkin(){
        System.out.println("skinManager appOrangeredSkinSkin ");
        setSelectedSkin("appRedSkin.css");
        return null;
    }
    public String blueSkin(){
        System.out.println("skinManager blueSkin ");
        setSelectedSkin("appBlueSkin.css");
        return null;
    }
}

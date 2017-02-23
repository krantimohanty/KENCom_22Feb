package com.swash.kencommerce.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kranti on 26/5/16.
 */
public class SlideMenuOption implements Serializable {


    private String name = "";
    private int id = 0;
    private int image = 0;
    private ArrayList<SlideMenuOption> slideMenuOptionsChild = new ArrayList<>();

    public ArrayList<SlideMenuOption> getSlideMenuOptionsChild() {
        return slideMenuOptionsChild;
    }

    public void setSlideMenuOptionsChild(ArrayList<SlideMenuOption> slideMenuOptionsChild) {
        this.slideMenuOptionsChild = slideMenuOptionsChild;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}

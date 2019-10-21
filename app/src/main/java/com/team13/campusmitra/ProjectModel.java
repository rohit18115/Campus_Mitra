package com.team13.campusmitra;

import java.util.ArrayList;
import java.util.List;

public class ProjectModel {
    private int img;
    private String title;
    private String description;
    private String link;
    private boolean expanded;
    public int getImg() {
        return img;
    }
    public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDesc() {
        return description;
    }
    public void setDesc(String description) {
        this.description = description;
    }
    public String getLink() {
        return link;
    }
    public void setPLink(String link) {
        this.link = link;
    }
    public boolean isExpanded() {
        return expanded;
    }
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
    public static List<ProjectModel> getObjectList() {
        List<ProjectModel> dataList = new ArrayList<>();
        int[] images = getImages();

        for (int i = 0; i < images.length; i++) {
            ProjectModel project = new ProjectModel();
            project.setImg(images[i]);
            project.setDesc("Project description here");
            project.setExpanded(false);
            project.setPLink("Project link: http://");
            int j = i+1;
            project.setTitle("Project " + j);
            dataList.add(project);
        }
        return dataList;
    }

    private static int[] getImages() {

        int[] images = {
                R.drawable.project_icon_1, R.drawable.project_icon_2, R.drawable.project_icon_3,
                R.drawable.project_icon_4, R.drawable.project_icon_5, R.drawable.project_icon_6,
                R.drawable.project_icon_7, R.drawable.project_icon_8, R.drawable.project_icon_9,
                R.drawable.project_icon_10
        };

        return images;
    }
}

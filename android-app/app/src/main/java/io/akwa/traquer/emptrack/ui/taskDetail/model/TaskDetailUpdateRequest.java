package io.akwa.traquer.emptrack.ui.taskDetail.model;

import java.util.ArrayList;
import java.util.List;

import io.akwa.traquer.emptrack.common.cloudinary.CloudinaryImage;

/**
 * Created by niteshgoel on 11/27/17.
 */

public class TaskDetailUpdateRequest {

    private String notes;
    private List<CloudinaryImage> images = new ArrayList<>();

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<CloudinaryImage> getImages() {
        return images;
    }

    public void setImages(List<CloudinaryImage> images) {
        this.images = images;
    }
}

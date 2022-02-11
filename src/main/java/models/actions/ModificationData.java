package models.actions;

import models.Data;
import models.DiffRow;
import org.eclipse.jgit.lib.PersonIdent;

import java.time.LocalDateTime;
import java.util.Date;

public abstract class ModificationData extends Data {

    public ModificationData() {
        this.isOnParent = true;
    }
    public abstract void setAdditionalData(String... additionalData);
}

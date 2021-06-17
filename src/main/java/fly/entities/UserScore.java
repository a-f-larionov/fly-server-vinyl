package fly.entities;

import vinyl.dependencies.entity.Entity;
import vinyl.dependencies.entity.ID;

@Entity
public class UserScore {

    @ID
    long id;

    long userId;

    int score;
}

package ml.ilei.moneybox.domains;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "receipts")
public class Receipts {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String sId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    private Date date;
    private float amount;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;
    private String description;

    public Long getId() {
        return id;
    }

    public String getsId() {
        return sId;
    }

    public void setsId() {
        this.sId = "" + id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

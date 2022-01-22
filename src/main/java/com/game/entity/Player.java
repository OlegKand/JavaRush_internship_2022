package com.game.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "player")
public class Player {

    private Long id;                        // ID игрока
    private String name;                    // Имя персонажа (до 12 знаков включительно)
    private String title;                   // Титул персонажа (до 30 знаков включительно)
    private Race race;                      // Расса персонажа
    private Profession profession;          // Профессия персонажа
    private Integer experience;             // Опыт персонажа. Диапазон значений 0..10,000,000
    private Integer level;                  // Уровень персонажа
    private Integer untilNextLevel;         // Остаток опыта до следующего уровня
    private Date birthday;                  // Дата регистрации. Диапазон значений года 2000..3000 включительно
    private Boolean banned;                 // Забанен (true) / не забанен (false)

    public Player() {
    }

    public Player(String name, String title, Race race, Profession profession,
                  Date birthday, Boolean banned, Integer experience) {
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.experience = experience;
        this.birthday = birthday;
        this.banned = banned;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "race")
    @Enumerated(EnumType.STRING)
    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    @Column(name = "profession")
    @Enumerated(EnumType.STRING)
    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    @Column(name = "experience")
    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    @Column(name = "level")
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Column(name = "untilNextLevel")
    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    @Column(name = "birthday")
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Column(name = "banned")
    public Boolean isBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public void updateLevelAndExp(){
        Integer levelUp = ((int)(Math.sqrt(2500 + 200 * getExperience())) - 50 )/ 100;
        setLevel(levelUp);
        Integer untilNextLevelUp = 50 * (getLevel() + 1) * (getLevel() + 2) - getExperience();
        setUntilNextLevel(untilNextLevelUp);
    }


    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", race=" + race +
                ", profession=" + profession +
                ", experience=" + experience +
                ", level=" + level +
                ", untilNextLevel=" + untilNextLevel +
                ", birthday=" + birthday +
                ", banned=" + banned +
                '}';
    }

    public static void main(String[] args) {

    }
}

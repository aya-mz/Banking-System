package core.user;

public class UserProfile {

    private String first_name,father_name,last_name,mother_name,phone_number,national_number;

    public UserProfile(String first_name,String father_name,String last_name,String mother_name,String phone_number,String national_number) {
        this.first_name = first_name;
        this.father_name = father_name;
        this.last_name = last_name;
        this.mother_name = mother_name;
        this.phone_number = phone_number;
        this.national_number = national_number;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMother_name() {
        return mother_name;
    }

    public void setMother_name(String mother_name) {
        this.mother_name = mother_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getNational_number() {
        return national_number;
    }

    public void setNational_number(String national_number) {
        this.national_number = national_number;
    }
}

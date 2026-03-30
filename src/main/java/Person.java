public abstract class Person {
    protected int id;
    protected String name;

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Person() {
        System.out.println("[WARN] Person object created without parameters!");
    }

    public abstract void displayDetails();
    
    public int getId() { return id; }
    public String getName() { return name; }
}

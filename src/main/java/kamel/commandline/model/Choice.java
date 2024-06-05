package kamel.commandline.model;

public class Choice {
    private int number;
    private String label;
    private Runnable process;

    public Choice(int number, String label, Runnable process) {
        this.number = number;
        this.label = label;
        this.process = process;
    }

    public void process() { process.run(); }

    @Override
    public String toString() {
        return number + "-" + label;
    }
}

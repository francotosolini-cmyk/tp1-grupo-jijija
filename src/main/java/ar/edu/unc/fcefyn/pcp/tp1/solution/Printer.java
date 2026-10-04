package ar.edu.unc.fcefyn.pcp.tp1.solution;

import ar.edu.unc.fcefyn.pcp.tp1.api.PrinterSnapshot;
import ar.edu.unc.fcefyn.pcp.tp1.api.PrinterState;

public class Printer {

    private final String id;
    private final int row;
    private final int column;
    private PrinterState state;
    private int usageCount;
    private Integer assignedOrderId;

    public Printer(int row, int column) {
        this.row = row;
        this.column = column;
        this.id = String.format("P-%d-%d", row, column);
        this.state = PrinterState.AVAILABLE;
        this.usageCount = 0;
        this.assignedOrderId = null;
    }

    public String getId() {
        return id;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public synchronized PrinterState getState() {
        return state;
    }

    public synchronized int getUsageCount() {
        return usageCount;
    }

    public synchronized Integer getAssignedOrderId() {
        return assignedOrderId;
    }

    public synchronized void reserveForOrder(int orderId) {
        this.state = PrinterState.RESERVED;
        this.assignedOrderId = orderId;
        this.usageCount++;
    }

    public synchronized void release(boolean printFailed) {
        this.assignedOrderId = null;
        this.state = printFailed ? PrinterState.OUT_OF_SERVICE : PrinterState.AVAILABLE;
    }

    /** Devuelve una instantánea con el estado actual de la impresora (requerido por la API). */
    public synchronized PrinterSnapshot toSnapshot() {
        return new PrinterSnapshot(id, state, usageCount, assignedOrderId);
    }
}

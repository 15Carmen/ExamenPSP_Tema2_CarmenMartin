package ejercicio2;

/*
 * Elena lo siento mucho, pero no sé ni lo que estoy haciendo en este momento sinceramente
 */

class Colecta<T> {

    private T dato;

    synchronized public T get() {
        T result = this.dato;
        this.dato = null;
        return result;
    }

    synchronized public void put(T valor) {
        this.dato = valor;
    }

    synchronized boolean datoDisponible() {
        return (this.dato != null);
    }

}

class HiloProductor implements Runnable {

    final Colecta<Integer> colecta;
    String nombreHilo;

    HiloProductor(Colecta<Integer> cont, String nombreHilo) {
        this.colecta = cont;
        this.nombreHilo = nombreHilo;
    }

    @Override
    public void run() {
        for (int i = 1;; i++) {
            synchronized (this.colecta) {
                while (this.colecta.datoDisponible()) {
                    try {
                        this.colecta.wait();
                    } catch (InterruptedException ex) {
                    }
                }
                System.out.printf("El Hilo %s produce %s.\n", this.nombreHilo, i);
                this.colecta.put(i);
                this.colecta.notify();
            }
        }

    }
}

class HiloConsumidor implements Runnable {

    final Colecta<Integer> colecta;
    String nombreHilo;

    public HiloConsumidor(Colecta<Integer> colecta, String nombreHilo) {
        this.colecta = colecta;
        this.nombreHilo = nombreHilo;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this.colecta) {
                while (!this.colecta.datoDisponible()) {
                    try {
                        this.colecta.wait();
                    } catch (InterruptedException ex) {
                    }
                }
                Integer dato = this.colecta.get();
                this.colecta.notify();
                System.out.printf("El Hilo %s consume valor %d.\n", this.nombreHilo, dato);
            }
        }
    }
}


public class ProductorConsumidor {


    public static void main(String[] args) {

        Colecta<Integer> dinero = new Colecta<Integer>();
        Thread productor1 = new Thread(new HiloProductor(dinero,"P1"));
        Thread productor2 = new Thread(new HiloProductor(dinero,"P2"));
        Thread productor3 = new Thread(new HiloProductor(dinero,"P3"));
        Thread productor4 = new Thread(new HiloProductor(dinero,"P4"));
        Thread consumidor1 = new Thread(new HiloConsumidor(dinero,"C1"));
        Thread consumidor2 = new Thread(new HiloConsumidor(dinero,"C2"));
        Thread consumidor3 = new Thread(new HiloConsumidor(dinero,"C3"));
        Thread consumidor4 = new Thread(new HiloConsumidor(dinero,"C4"));
        productor1.start();
        productor2.start();
        productor3.start();
        productor4.start();
        consumidor1.start();
        consumidor2.start();
        consumidor3.start();
        consumidor4.start();
    }
}

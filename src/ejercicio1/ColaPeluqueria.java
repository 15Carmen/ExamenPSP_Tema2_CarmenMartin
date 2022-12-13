package ejercicio1;

import java.util.concurrent.Semaphore;

public class ColaPeluqueria implements Runnable {
    public static Semaphore semaforoPeluqueria = new Semaphore(4);
    public static Semaphore semaforoBarbero = new Semaphore(2);
    private boolean sillaOcupada = false;

    /**
     * Metodo que usaremos para saber que hilos se encuentran sentados en la peluquería
     */
    public void peluqueria() {
        try {
            semaforoPeluqueria.acquire();  // Adquirimos un permiso para pasar por el semáforo de las sillas de la peluquería
            //Imprimimos un mensaje diciendo que el hilo está sentado en la peluquería
            System.out.println(Thread.currentThread().getName() + " está sentado en la peluquería");
        } catch (InterruptedException e) {
            //Capturamos los errores
            System.err.println("Error, se ha interrumpido el hilo con código de error: " +  e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Metodo que usaremos para saber que hilos están siendo atendidos por los barberos
     */
    public void barbero(){
        try {
            semaforoBarbero.acquire();  //Adquirimos un permiso para pasar por el semáforo de los barberos
            //Imprimimos un mensaje diciendo que el hilo está siendo atendido
            System.out.println(Thread.currentThread().getName() + " está siendo atendido");
            Thread.sleep ((long) (Math.random() * (5000 - 1000) + 1000)); //Espera entre 1 y 5 segundos
            //Imprimimos un mensaje diciendo que el hilo ha terminado en la peluquería
            System.out.println(Thread.currentThread().getName() + " ha terminado en la peluqueria");
            semaforoBarbero.release();  //Dejamos libre el permiso del semáforo que estabamos ocupando de las sillas de la peluquería
            semaforoPeluqueria.release();  //Dejamos libre el permiso del semáforo que estabamos ocupando de los barberos
        } catch (InterruptedException e) {
            System.err.println("Error, se ha interrumpido el hilo con código de error: " +  e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Reescribimos el metodo run
     */
    @Override
    public void run() {
        while (!sillaOcupada) {  //Mientras que la silla esté ocupada
            if (semaforoPeluqueria.availablePermits() > 0 && !sillaOcupada) { //Si hay sillas libres y están ocupadas
                peluqueria(); //Llamamos al metodo peluquería
                sillaOcupada = true; //Indicamos que la silla está ocupada
                if (semaforoBarbero.availablePermits() > 0 && sillaOcupada){ //Si hay barberos libres y las sillas están ocupadas
                    barbero(); //Llamamos al metodo barbero
                }
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {                     //Generamos 10 hilos
            Thread hilo = new Thread(new ColaPeluqueria());
            hilo.setName("Hilo " + i);                      //Le damos un nombre a los hilos
            hilo.start();                                   //Iniciamos los hilos
        }

    }

}
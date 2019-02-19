# Parking-Lot
[![CircleCI](https://circleci.com/gh/GabrielEValenzuela/Parking-Lot/tree/master.svg?style=svg&circle-token=b265cad4ba8e9e929a2c4eeb8d78a5183e9dae21)](https://circleci.com/gh/GabrielEValenzuela/Parking-Lot/tree/master)
### Enunciado:
En este práctico se debe resolver el control de acceso a una playa de estacionamiento con 3 (tres) entradas (calles) diferentes. En esta playa hay 2 pisos, y en cada  piso pueden  estacionar 30 autos. La playa cuenta con 2 salidas diferentes y una única estación de pago (caja). En los accesos a la playa y en los egresos existen barreras que deben modelarse.
La playa cuenta con 3(tres) lugares donde los vehículos se detienen cuando quieren entrar (barrera), una vez que ingresaron se les indica un piso y estacionan (Puede ser el 1er piso o el 2do).
Se debe cuidar que no se permita el ingreso (superar barrera) a más vehículos de los espacios disponibles totales.

La playa cuenta con 3(tres) lugares donde los vehículos se detienen cuando quieren entrar (barrera), una vez que ingresaron se les indica un piso y estacionan (Puede ser el 1er piso o el 2do).
Se debe cuidar que no se permita el ingreso (superar barrera) a más vehículos de los espacios disponibles totales.

Los autos que se retiran de la playa deben liberar un espacio del piso en que se encontraban (Diferenciar estacionamiento en cada piso). Cuando un vehículo se va a retirar se puede optar por la salida en la calle 1 o salida a la calle 2.
Luego debe abandonar la estadía. El cobro lo lleva a cabo un empleado promedio que tarda al menos 3[min] (Existe una sola caja).

En  caso  de  que  la  playa  esté llena,  se  debe  encender  un  cartel  luminoso
externo que  indica  tal situación. El  sistema  controlador debe  estar conformado  por distintos hilos, los  cuales  deben  ser asignados a cada  conjunto  de  responsabilidades  afines  en  particular.  e.g.  Ingreso de  vehículos,  manejo  de barreras, etc.

### Tareas a realizar:
1. Red de Petri que modela al sistema
2. Agregar las restricciones necesarias para evitar interbloqueos ni accsos cuando no hay lugar, mostrando con la herramienta elegida y justifciarlo.
3. Simular la solución en un proyecto desarrollado con la herramienta adecuada (Explicar porqué el uso).
4. Colocar tiempo en la estación de pago caja (En la/s transición/es corresponidente/s)
5. Tabla de eventos
6. Tabla de estados o actividades
7. Cantidad de hilos (Justificar)
8. Implementar dos casos de políticas para:
   - Prioridad de llenar de vehículas la planta baja (1er piso) y luego el piso superior con prioridad de salida indistitna (caja)
   - Prioridad de llenado indistinto, pero salida a la calle 2.
9. Diagramas de clases
10. Diagrama de secuencia
11. Código
12. Testing

✅ STEP 3 — DECLARE the Shooter object
“Declaring is like telling the robot ‘yes, we will have a shooter system’.”

Add this at the top of your TeleOp class:
///////////////////
Shooter shooter;


No parenthesis yet — this is only the declaration.

✅ STEP 4 — INITIALIZE the Shooter (in init())

“Initialize means we actually create the shooter and connect it to the hardware.”

Inside init() write:
////////////////////////////
@Override
public void init() {
    shooter = new Shooter(hardwareMap);  // connects shooter to the motors
}


This runs once, before TeleOp starts.

✅ STEP 5 — USE the Shooter in loop()

“The loop keeps running 30–50 times per second.
Buttons change shooter RPM.
Update() keeps the shooter working.”

Example:

@Override
public void loop() {

    // A button → turn shooter on
    if (gamepad1.a) {
        shooter.setTargetRPM(3000);
    }

    // B button → stop
    if (gamepad1.b) {
        shooter.stop();
    }

    // MUST run every loop
    shooter.update();

    telemetry.addData("Target RPM", shooter.getTargetRPM());
    telemetry.addData("Current RPM", shooter.getCurrentRPM());
    telemetry.update();
}

ПОТОМ ЧЕРЕЗ ЧАТГПТ МОЖЕШЬ ДОБАВИТЬ ИЗМЕНЕНИЕ СКОРОСТИ СТРЕЛЬБЫ НА КНОПКИ
ДОБАВИТЬ АЛГОРИТМ КОТОРЫЙ НА ОДНУ КНОПКУ ОТКРОЕТ СЕРВО И ЗАПУСТИТ ИНТЕЙК ЧТОБЫ ВЫСТРЕЛИТЬ

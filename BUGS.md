org.ronobot.engine.App.start() will call the start method on Game game and then exits, even though Game.start() does not create a game loop. Game also does not have rendering.
Either add a loop to Game class or to App.start
Also add rendering capability to Game

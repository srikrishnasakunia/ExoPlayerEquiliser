# Learning So Far

### 1. remember{}
Remember block is used for Caching values and reusing the value/variable. Basically we store the 
reference of the object and not the value of the object and when we need that value we take it from
that reference rather than recalculating it whent the composable re-composes/ re-renders it.

#### Working of Remember block
When a composable function with remember block is executed for the first time, the block within is
executed first and the resulting object reference is stored in the compose internal cache. When the
Composition recomposes, we get the updated value since we are referring to the reference of the 
object instead of the value.
When the composition is recomposed, we check if there exists a cache value of the object, if yes we 
use it instead of calculating/executing the value.


### 2. LaunchedEffect {}
LaunchedEffect helps us to run Asynchronous operations (suspended Function) within composable funcs.
The code written in this block are lifecycle aware and get cancelled once the composable func leaves
the composition. These are generally used for data calls from DB or Network, performing animations,
setting up timers/delays & observing system events

#### Parameters
This function takes in two parameters i.e., key1 and key2. Whenever the value of these key changes 
the entire function is re-executed meaning the effect is restarted by cancelling the previous 
coroutine & starting a new one.
##### LaunchedEffects are useful due to following features
a. These coroutines get cancelled when the composable is removed, meaning they are lifecycle aware.
b. We can launch multiple coroutines from a single launchedEffect.
c. We can use Key parameters to define values based on which we want to restart the effects.

LaunchedEffect isn't a coroutine, its a composable function which helps us to manage SideEffects with 
the help of Coroutine within the composable. 

### 3. Side Effects
Side Effects are basically Actions that take place outside the the declarative world of composable,
like performing network calls, accessing DB, or writing to the file. LaunchedEffect is one such 
example of Side Effects.

### 4. AndroidView
AndroidView is commonly needed for using Views that are infeasible to be reimplemented in Compose 
and there is no corresponding Compose API. Common examples for the moment are WebView, SurfaceView, 
AdView, etc.
AndroidView is a interop or interoperability library provided to use Custom Android Views that are 
yet not availabe for Compose. Same thing can be done with Compose views whileusing them in 
Normal View (XML).

AndroidView takes in a factory parameter which takes in Context and returns the rendered View 
instance. This Factory function is only executed when the view is initially created or when the 
parent composable recomposes.
State updates within factory wont trigger recomposition, we need to use the update callback for 
dynamic updates.

### 5. PlayerView
PlayerView is a default View in Compose used for Creating default video players.
Consider using Player for creating custom video players and it doesn't require AndroidView as parent.

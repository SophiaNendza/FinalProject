# Deadline

Modify this file to satisfy a submission requirement related to the project
deadline. Please keep this file organized using Markdown. If you click on
this file in your GitHub repository website, then you will see that the
Markdown is transformed into nice-looking HTML.

## Part 1.1: App Description

 My app allows users to search for pokemon cards and
 display the image and the type.  If there are multiple cards
 with that pokemon the user can switch between them with arrows.
 A random button is also provided for users who just want to look at random
 pokemon cards.

 The apis used are Pokemon TCG and Pokeapi.  These 2 were connected in the app. When the user searches for a pokemon the name is parsed in the request for Pokemon TCG.  The api returns images and types for that pokemon.  The image is displayed and the other images can be displayed with the arrow buttons.  The type is then parsed to the request to the Pokeapi which returns the type in different languages.  If the user presses the random button, a request to the Pokeapi is made to return an array of all the pokemon.  From that response a random name is chosen and is parsed to the Pokemon TCG request which then returns the cards with that name.

https://github.com/SophiaNendza/FinalProject

## Part 1.2: APIs

> For each RESTful JSON API that your app uses (at least two are required),
> include an example URL for a typical request made by your app. If you
> need to include additional notes (e.g., regarding API keys or rate
> limits), then you can do that below the URL/URI. Placeholders for this
> information are provided below. If your app uses more than two RESTful
> JSON APIs, then include them with similar formatting.

### API 1

```
https://api.pokemontcg.io/v2/cards?q=name:pikachu
```
API key = c2566a44-fab6-4a73-8fbd-45cc9b93a2df
### API 2

```
https://pokeapi.co/api/v2/type/3
```
```
https://pokeapi.co/api/v2/pokedex/1/pikachu
```
Both of these requests are made one for the type the other for the pokedex which is used for the random button

## Part 2: New

> What is something new and/or exciting that you learned from working
> on this project?

Connecting 2 apis and how to naviage 2 different apis that are formatted differently.  Decorating the GUI with gifs and background colors was fun!

## Part 3: Retrospect

> If you could start the project over from scratch, what do
> you think might do differently and why?

Add more functions to the project that filled up more space.  Take more time with the GUI and make it prettier.
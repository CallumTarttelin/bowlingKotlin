import * as React from "react";
import * as ReactDom from "react-dom";
import {BrowserRouter as Router, Route} from "react-router-dom";
import "../resources/style/style.css";
import AddGame from "./add/AddGame";
import AddLeague from "./add/AddLeague";
import AddPlayer from "./add/AddPlayer";
import AddTeam from "./add/AddTeam";
import Game from "./domain/Game";
import League from "./domain/League";
import Team from "./domain/Team";
import LeagueList from "./other/LeagueList";

function App() {
    return (
        <Router>
            <div id="app">
                <Route exact={true} path="/" component={LeagueList} />
                <Route exact={true} path="/league" component={LeagueList} />
                <Route exact={true} path="/league/:id/" component={League}/>
                <Route exact={true} path="/team/:id/" component={Team}/>
                <Route exact={true} path="/game/:id/" component={Game}/>
                <Route path="/league/:id/add-team" component={AddTeam}/>
                <Route path="/league/:id/add-game" component={AddGame}/>
                <Route path="/team/:id/add-player" component={AddPlayer}/>
                <Route path="/add/" component={AddLeague} />
            </div>
        </Router>
    );
}

ReactDom.render(
    <App />,
    document.getElementById("react"),
);

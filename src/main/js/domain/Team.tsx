import Button from "@material-ui/core/Button/Button";
import CircularProgress from "@material-ui/core/CircularProgress/CircularProgress";
import axios from "axios/index";
import * as React from "react";
import {Link} from "react-router-dom";
import ErrorMessage from "../other/ErrorMessage";
import Header from "../other/Header";
import SortedGameList from "../other/SortedGameList";
import PlayerPanel from "./PlayerPanel";

export interface TeamProps {
    match: {params: {id: string}};
}

interface Game {
    id: number;
    venue: string;
    time: string;
    teams: Array<{name: string, id: number}>;
    winner: number;
}

export interface TeamState {
    status: string;
    error: object;
    id: string;
    name: string;
    league: {name: string, id: number};
    players: Array<{name: string, id: number}>;
    games: Game[];
    numGames: number;
    pinsFor: number;
    pinsAgainst: number;
    highHandicapGame: number;
    highHandicapSeries: number;
    teamPoints: number;
    totalPoints: number;
}

export default class Team extends React.Component<TeamProps, TeamState> {

    constructor(props: any) {
        super(props);
        this.state = {
            status: "Loading",
            error: {},
            id: props.match.params.id,
            name: "Error Not Loaded",
            league: {name: "Error Not Loaded", id: -1},
            players: [],
            games: [],
            numGames: 0,
            pinsFor: 0,
            pinsAgainst: 0,
            highHandicapGame: 0,
            highHandicapSeries: 0,
            teamPoints: 0,
            totalPoints: 0,
        };
        this.getTeam = this.getTeam.bind(this);
        this.switchTeam = this.switchTeam.bind(this);
        this.getTeam(props.match.params.id);
    }

    public getTeam(id: string) {
        axios.get("/api/team/" + id)
            .then((response) => {
                this.setState({
                    status: "OK",
                    id,
                    name: response.data.name,
                    league: response.data.league,
                    players: response.data.players,
                    games: response.data.games,
                    numGames: response.data.numGames,
                    pinsFor: response.data.pinsFor,
                    pinsAgainst: response.data.pinsAgainst,
                    highHandicapGame: response.data.highHandicapGame,
                    highHandicapSeries: response.data.highHandicapSeries,
                    teamPoints: response.data.teamPoints,
                    totalPoints: response.data.totalPoints,
                });
            })
            .catch((error) => {
                this.setState({error, status: "error"});
            });
    }

    public render() {
        const {status, error, id, name, league, players, games} = this.state;
        if (status === "error") {
            return <ErrorMessage error={error}/>;
        } else if (status === "Loading") {
            return <CircularProgress color={"primary"} />;
        }
        return (
            <div className={"Team"}>

                <Header title={name} back={`Back to ${league.name}`} backLocation={`/league/${league.id}`}/>

                <table className={"Stats"}>
                    <thead>
                    <tr>
                        <th>Games</th>
                        <th>Pins For</th>
                        <th>Pins Against</th>
                        <th>HHG</th>
                        <th>HHS</th>
                        <th>Team Pts</th>
                        <th>Total Pts</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>{this.state.numGames}</td>
                        <td>{this.state.pinsFor}</td>
                        <td>{this.state.pinsAgainst}</td>
                        <td>{this.state.highHandicapGame}</td>
                        <td>{this.state.highHandicapSeries}</td>
                        <td>{this.state.teamPoints}</td>
                        <td>{this.state.totalPoints}</td>
                    </tr>
                    </tbody>
                </table>
                <h3>Players</h3>

                <ul className={"Players"}>
                    {players.map((player, index) => (
                        <PlayerPanel key={index} player={player} changeTeam={this.switchTeam} />
                    ))}
                </ul>
                <Link to={{pathname: ("/team/" + id + "/add-player"), state: {teamName: name}}}>
                    <Button className={"addPlayer"} variant={"raised"} color={"primary"}>Add A Player</Button>
                </Link>

                <SortedGameList games={games} changeTeamCallback={this.switchTeam} />
            </div>
        );
    }

    private switchTeam(event: any, id: number) {
        if (this.state.id !== id.toString() && event.button === 0) {
            this.setState({status: "Loading"});
            this.getTeam(id.toString());
        }
    }
}

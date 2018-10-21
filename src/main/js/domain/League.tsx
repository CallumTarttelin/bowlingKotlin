import Button from "@material-ui/core/Button/Button";
import CircularProgress from "@material-ui/core/CircularProgress/CircularProgress";
import axios from "axios";
import * as React from "react";
import {Component} from "react";
import {Link} from "react-router-dom";
import ErrorMessage from "../other/ErrorMessage";
import Header from "../other/Header";
import SortedGameList from "../other/SortedGameList";
import TeamSummary from "../summary/TeamSummary";

interface Team {
    id: number;
    name: string;
    numGames: number;
    pinsFor: number;
    pinsAgainst: number;
    highHandicapGame: number;
    highHandicapSeries: number;
    teamPoints: number;
    totalPoints: number;
}

interface Game {
    id: number;
    venue: string;
    time: string;
    teams: Array<{name: string, id: number}>;
    winner: number;
}

export interface  LeagueProps {
    match: {params: {id: string}};
}

export interface LeagueState {
    status: string;
    id: string;
    name: string | null;
    teams: Team[];
    games: Game[];
    error: object;
}

export default class League extends Component<LeagueProps, LeagueState> {
    public readonly state = {status: "Loading", id: "", name: "", teams: [], games: [], error: {}};

    constructor(props: any) {
        super(props);
        this.getLeague = this.getLeague.bind(this);
        this.teamTable = this.teamTable.bind(this);
        this.getLeague(props.match.params.id);
    }

    public getLeague(id: string) {
        axios.get(`/api/league/${id}`)
            .then((response: {data: {name: string, teams: Team[], games: Game[]}}) => {
                this.setState({
                    games: response.data.games,
                    id,
                    name: response.data.name,
                    status: "OK",
                    teams: response.data.teams,
                });
            })
            .catch((error) => {
                this.setState({error, status: "Error"});
                });
    }

    public render(): JSX.Element {
        if (this.state.status === "Error") {
            return (
                <div id={"league"}>
                    <Header title={this.state.name} back={"Back to Leagues"} backLocation={"/league"}/>
                    <ErrorMessage error={this.state.error} />
                </div>
            );
        }
        if (this.state.status === "Loading") {
            return (
                <div id={"league"}>
                    <Header title={"League Loading."} back={"Back to Leagues"} backLocation={"/league"}/>
                    <CircularProgress color={"primary"} />
                </div>
            );
        }
        return (
            <div id={"league"}>
                <Header title={this.state.name} back={"Back to Leagues"} backLocation={"/league"}/>
                <h3>Team Standings!</h3>
                {this.teamTable()}
                <Link to={{pathname: ("/league/" + this.state.id + "/add-team"), state: {leagueName: this.state.name}}}>
                    <Button className={"addTeam"} variant={"raised"} color={"primary"}>Add A Team</Button>
                </Link>

                <SortedGameList games={this.state.games} />

                <Link
                    to={{
                        pathname: ("/league/" + this.state.id + "/add-game"),
                        state: {league: {name: this.state.name, teams: this.state.teams, id: this.state.id}},
                    }}
                >
                    <Button className={"addTeam"} variant={"raised"} color={"primary"}>Add A Game</Button>
                </Link>
            </div>
        );
    }

    private teamTable(): JSX.Element {
        return (
            <table className={"teams"}>
                <thead>
                    <tr>
                        <th style={{ width: "5%" }} >Position</th>
                        <th style={{ width: "20%" }}>Team</th>
                        <th style={{ width: "10%" }}>Games</th>
                        <th style={{ width: "10%" }}>Pins For</th>
                        <th style={{ width: "10%" }}>Pins Against</th>
                        <th style={{ width: "10%" }}>HHG</th>
                        <th style={{ width: "10%" }}>HHS</th>
                        <th style={{ width: "10%" }}>Team Pts</th>
                        <th style={{ width: "10%" }}>Total Pts</th>
                        <th style={{ width: "5%" }} className={"invis"} />
                    </tr>
                </thead>
                {this.state.teams.map((team: Team, index: number) => (
                    <TeamSummary
                        key={team.id}
                        id={team.id}
                        position={index + 1}
                        numGames={team.numGames}
                        pinsFor={team.pinsFor}
                        pinsAgainst={team.pinsAgainst}
                        highHandicapGame={team.highHandicapGame}
                        highHandicapSeries={team.highHandicapSeries}
                        teamPoints={team.teamPoints}
                        totalPoints={team.totalPoints}
                        leagueName={this.state.name}
                    >
                        {team.name}
                    </TeamSummary>
                ))}
            </table>
        );
    }
}

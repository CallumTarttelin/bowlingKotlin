import Button from "@material-ui/core/Button/Button";
import InputLabel from "@material-ui/core/InputLabel/InputLabel";
import MenuItem from "@material-ui/core/MenuItem/MenuItem";
import Select from "@material-ui/core/Select/Select";
import TextField from "@material-ui/core/TextField/TextField";
import axios from "axios";
import * as React from "react";
import {Component} from "react";
import ErrorMessage from "../other/ErrorMessage";
import Header from "../other/Header";

export interface AddGameState {
    error: object | null;
    league: League;
    team1: number;
    team2: number;
    time: string;
    venue: string;
    errorMessage: string;
}

interface League {
    id: number | string;
    teams: Array<{name: string, id: number}>;
    name: string;
}

export interface AddGameProps {
    match: {params: {id: string}};
    location: {state?: {league?: League}};
}

export default class AddGame extends Component<AddGameProps, AddGameState> {

    constructor(props: AddGameProps) {
        super(props);
        this.state = {
            error: null,
            league: props.location.state !== undefined  && props.location.state.league !== undefined ?
                props.location.state.league :
                {
                    id: props.match.params.id,
                    teams: [],
                    name: "",
                },
            team1: 0,
            team2: 0,
            time: new Date().toISOString().slice(0, -5),
            venue: "",
            errorMessage: "",
        };
        this.submit = this.submit.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.getLeague = this.getLeague.bind(this);
        if (this.state.league.name === "") {
            this.getLeague();
        }
    }

    public getLeague() {
        const id = this.state.league.id;
        axios.get(`/api/league/${id}`)
            .then((response) => {
                this.setState({league: {
                    id,
                    teams: response.data.teams,
                    name: response.data.name,
                }});
            })
            .catch((error) => {
                this.setState({error});
            });
    }

    public isValid() {
        if (this.state.team1 === this.state.team2) {
            return "Teams cannot be the same!";
        } else if (this.state.venue === "") {
            return "Venue cannot be empty";
        } else if (isNaN(Date.parse(this.state.time))) {
            return "Date is not valid";
        } else {
            return "";
        }
    }

    public submit(event: any) {
        event.preventDefault();
        const {venue, time, league} = this.state;
        const teamIds = [this.state.team1, this.state.team2];
        if (this.isValid() !== "") {
            this.setState({errorMessage: this.isValid()});
            return;
        }
        axios.post("/api/game", {leagueId: league.id, venue, time, teamIds})
            .then(() => {
                window.location.href = "/league/" + league.id;
            })
            .catch((error) => {
                if (error.response.status === 401) {
                    window.location.href = "/login";
                } else {
                    this.setState({ error });
                }
            });
    }

    public handleChange = (event: {target: {value: string, name: string}}) => {
        // @ts-ignore
        this.setState({[event.target.name]: event.target.value});
    }

    public render() {
        const {error, team1, team2, time, venue} = this.state;
        const {id, teams, name} = this.state.league;
        return (
            <div className={"AddScreen"}>
                <Header title={`Add game to ${name}`} back={name} backLocation={`/league/${id}`}/>

                <form className={"theGameForm"} onSubmit={this.submit} noValidate={true}>

                    <TextField
                        id="Venue"
                        label="Venue"
                        name={"venue"}
                        value={venue}
                        placeholder="Venue"
                        className={"VenueInput"}
                        onChange={this.handleChange}
                        autoFocus={true}
                    />

                    <br />
                    <br />

                    <TextField
                        id="datetime-local"
                        type="datetime-local"
                        className={"TimeInput"}
                        value={time}
                        name={"time"}
                        onChange={this.handleChange}
                    />

                    <br />
                    <br />

                    <InputLabel htmlFor="team-1">team1</InputLabel>
                    <Select
                        value={team1}
                        onChange={this.handleChange}
                        id="team1"
                        className={"team1"}
                        inputProps={{
                            name: "team1",
                            id: "team1",
                        }}
                    >
                        <MenuItem value={0}><em>None</em></MenuItem>
                        {teams.map((team) => (
                            <MenuItem
                                key={team.id}
                                value={team.id}
                                name={`team1-${team.name.replace(/\s+/g, "-").toLowerCase()}`}
                            >
                                {team.name}
                            </MenuItem>
                        ))}
                    </Select>

                    <br />
                    <br />

                    <InputLabel htmlFor="team-2">team1</InputLabel>
                    <Select
                        value={team2}
                        onChange={this.handleChange}
                        id="team2"
                        className={"team2"}
                        inputProps={{
                            name: "team2",
                            id: "team2",
                        }}
                    >
                        <MenuItem value={0}><em>None</em></MenuItem>
                        {teams.map((team) => (
                            <MenuItem
                                key={team.id}
                                value={team.id}
                                name={`team2-${team.name.replace(/\s+/g, "-").toLowerCase()}`}
                            >
                                {team.name}
                            </MenuItem>
                        ))}
                    </Select>

                    <br />
                    <br />

                    <Button type={"submit"} variant={"raised"} color={"primary"} className={"submit"}>Submit</Button>
                    <ErrorMessage error={error}/>
                    <p className={"errorMessage"}>{this.state.errorMessage}</p>
                </form>
            </div>
        );
    }
}

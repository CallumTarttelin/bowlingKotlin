import Button from "@material-ui/core/Button/Button";
import CircularProgress from "@material-ui/core/CircularProgress/CircularProgress";
import InputLabel from "@material-ui/core/InputLabel/InputLabel";
import MenuItem from "@material-ui/core/MenuItem/MenuItem";
import Select from "@material-ui/core/Select/Select";
import axios from "axios";
import * as React from "react";
import {Component} from "react";
import ErrorMessage from "../other/ErrorMessage";

export interface AddPlayersProps {
    gameId: number;
    team: {name: string, id: number};
    refresh?: () => void;
}

export interface AddPlayersState {
    gameId: number;
    error: object | null;
    errorMessage: string;
    team: {name: string, id: number};
    players: Array<{name: string, id: number}>;
    player1: number;
    player2: number;
    player3: number;
}

export default class AddPlayers extends Component<AddPlayersProps, AddPlayersState> {
    constructor(props: any) {
        super(props);
        this.submit = this.submit.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.isValid = this.isValid.bind(this);
        this.state = {
            gameId: props.gameId,
            error: null,
            errorMessage: "",
            team: props.team,
            players: [],
            player1: 0,
            player2: 0,
            player3: 0,
        };
        this.getPlayers();
    }

    public isValid() {
        const playerNames = [this.state.player1, this.state.player2, this.state.player3];
        return (playerNames.indexOf(0) <= -1 &&
            playerNames.length === new Set(playerNames).size);
    }

    public getPlayers() {
        axios.get(`/api/team/${this.state.team.id}`)
            .then( (response) => {
                this.setState({players: response.data.players});
            })
            .catch((error) => {
                this.setState({error});
            });
    }

    public submit(event: any) {
        event.preventDefault();
        const playerIds = [this.state.player1, this.state.player2, this.state.player3];
        if (this.isValid()) {
            axios.post("/api/playergame", { teamId: this.state.team.id, gameId: this.state.gameId, playerIds })
                .then(() => {
                    if (this.props.refresh !== undefined) {
                        this.props.refresh();
                    } else {
                        location.reload();
                    }
                })
                .catch((error) => {
                    if (error.response.status === 401) {
                        window.location.href = "/login";
                    } else {
                        this.setState({ error });
                    }
                });
        } else {
            this.setState({errorMessage: "Not valid teams, check for duplicates or unset"});
        }
    }

    public handleChange = (event: {target: {value: string, name: string}}) => {
        // @ts-ignore
        this.setState({[event.target.name]: event.target.value});
    }

    public genInput(id: number) {
        return(
            <React.Fragment>
                <InputLabel htmlFor={`player-${id}`}>player1</InputLabel>
                <Select
                    // @ts-ignore
                    value={this.state[`player${id}`]}
                    onChange={this.handleChange}
                    id={`player${id}`}
                    className={`player${id}`}
                    inputProps={{
                        name: `player${id}`,
                        id: `player${id}`,
                    }}
                >
                    <MenuItem value={0}><em>None</em></MenuItem>
                    {this.state.players.map((player) => (
                        <MenuItem
                            key={player.id}
                            value={player.id}
                            name={`player${id}-` + player.name.replace(/\s+/g, "-").toLowerCase()}
                        >
                            {player.name}
                        </MenuItem>
                    ))}
                </Select>
                <br />
                <br />
            </React.Fragment>
        );
    }

    public render() {
        const {error, players} = this.state;
        if (players.length === 0) {
            return <React.Fragment><CircularProgress color={"primary"} /><ErrorMessage error={error}/></React.Fragment>;
        }
        return (
            <div className={"AddScreen"}>
                <h4>{`Add Players for ${this.state.team.name}`}</h4>
                <form className={"thePlayersForm"} onSubmit={this.submit} noValidate={true}>

                    {this.genInput(1)}
                    {this.genInput(2)}
                    {this.genInput(3)}

                    <Button type={"submit"} variant={"raised"} color={"primary"} className={"submit"}>Submit</Button>

                </form>
                <ErrorMessage error={error}/>
                <p className={"errorMessage"}>{this.state.errorMessage}</p>
            </div>
        );
    }
}

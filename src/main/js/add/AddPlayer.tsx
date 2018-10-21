import Button from "@material-ui/core/Button/Button";
import TextField from "@material-ui/core/TextField/TextField";
import axios from "axios";
import * as React from "react";
import {Component} from "react";
import ErrorMessage from "../other/ErrorMessage";
import Header from "../other/Header";

export interface AddPlayerProps {
    match: {params: {id: string}};
    location: {state: {teamName?: string}};
}

export interface AddPlayerState {
    error: object | null;
    teamName: string;
    playerName: string;
    id: string;
    errorMessage: string;
}

export default class AddPlayer extends Component<AddPlayerProps, AddPlayerState> {
    constructor(props: any) {
        super(props);
        this.submit = this.submit.bind(this);
        this.getTeam = this.getTeam.bind(this);
        this.state = {
            error: null,
            id: props.match.params.id,
            teamName: props.location.state.teamName ? props.location.state.teamName : "",
            playerName: "",
            errorMessage: "",
        };
        if (this.state.teamName === "") {
            this.getTeam();
        }
    }

    public getTeam() {
        axios.get(`/api/team/${this.state.id}`)
            .then((response: {data: {name: string}}) => {
                this.setState({
                    teamName: response.data.name,
                });
            })
            .catch((error) => {
                this.setState({error});
            });
    }

    public isValid(): string {
        if (this.state.playerName === "") {
            return "Player name cannot be blank";
        } else {
            return "";
        }
    }

    public submit(event: any) {
        event.preventDefault();
        if (this.isValid() !== "") {
            this.setState({errorMessage: this.isValid()});
            return;
        }
        axios.post("/api/player", { teamId: this.state.id, name: this.state.playerName })
            .then(() => {
                window.location.href = `/team/${this.state.id}`;
            })
            .catch((err) => {
                if (err.response.status === 401) {
                    window.location.href = "/login";
                } else {
                    this.setState({ error: err });
                }
            });
    }

    public handleChange = (event: {target: {value: string, id: string}}) => this.setState({
        playerName: event.target.value,
    })

    public render() {
        const {error, teamName, playerName, id} = this.state;
        return (
            <div>
                <Header
                    title={"Add a player" + (teamName !== "" ? ` to ${teamName}!` : "!")}
                    back={"Back" + (teamName !== "" ? ` to ${teamName}` : "")}
                    backLocation={`/team/${id}`}
                />
                <form className={"thePlayerForm"} onSubmit={this.submit}>
                    <TextField
                        id={"playerName"}
                        label={"Player Name"}
                        value={playerName}
                        onChange={this.handleChange}
                    />
                    <br />
                    <Button type={"submit"} color={"primary"} variant={"raised"}>Submit</Button>
                </form>
                <ErrorMessage error={error} />
                <p className={"errorMessage"}>{this.state.errorMessage}</p>
            </div>
        );
    }
}

import Button from "@material-ui/core/Button/Button";
import TextField from "@material-ui/core/TextField/TextField";
import axios from "axios";
import * as React from "react";
import {Component} from "react";
import ErrorMessage from "../other/ErrorMessage";
import Header from "../other/Header";

export interface AddTeamProps {
    match: {params: {id: string}};
    location: {state: {leagueName?: string}};
}

export interface AddTeamState {
    error: object | null;
    teamName: string;
    leagueName: string;
    id: string;
    errorMessage: string;
}

export default class AddTeam extends Component<AddTeamProps, AddTeamState> {
    constructor(props: any) {
        super(props);
        this.submit = this.submit.bind(this);
        this.getLeague = this.getLeague.bind(this);
        this.state = {
            error: null,
            id: props.match.params.id,
            leagueName: props.location.state.leagueName ? props.location.state.leagueName : "",
            teamName: "",
            errorMessage: "",
        };
        if (this.state.leagueName === "") {
            this.getLeague();
        }
    }

    public getLeague() {
        axios.get(`/api/league/${this.state.id}`)
            .then((response: {data: {name: string}}) => {
                this.setState({
                    leagueName: response.data.name,
                });
            })
            .catch((error) => {
                this.setState({error});
            });
    }

    public isValid(): string {
        if (this.state.teamName === "") {
            return "Team name cannot be blank";
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
        axios.post("/api/team", { leagueId: this.state.id, name: this.state.teamName })
            .then(() => {
                window.location.href = `/league/${this.state.id}`;
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
        teamName: event.target.value,
    })

    public render() {
        const {error, teamName, leagueName, id} = this.state;
        return (
            <div>
                <Header
                    title={"Add a team" + (leagueName !== "" ? ` to ${leagueName}!` : "!")}
                    back={"Back" + (leagueName !== "" ? ` to ${leagueName}` : "")}
                    backLocation={`/league/${id}`}
                />
                <form className={"theTeamForm"} onSubmit={this.submit}>
                    <TextField
                        id={"teamName"}
                        label={"Team Name"}
                        value={teamName}
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

import Button from "@material-ui/core/Button/Button";
import TextField from "@material-ui/core/TextField/TextField";
import axios from "axios";
import * as React from "react";
import {Component} from "react";
import ErrorMessage from "../other/ErrorMessage";
import Header from "../other/Header";

export interface AddLeagueState {
    error: object | null;
    leagueName: string;
    errorMessage: string;
}

export default class AddLeague extends Component<{}, AddLeagueState> {
    public readonly state = {error: null, leagueName: "", errorMessage: ""};

    constructor(props: any) {
        super(props);
        this.submit = this.submit.bind(this);
    }

    public isValid(): string {
        if (this.state.leagueName === "") {
            return "League name cannot be blank";
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
        axios.post("/api/league", { name: this.state.leagueName })
            .then(() => {
                window.location.href = "/";
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
            leagueName: event.target.value,
    })

    public render() {
        const {error, leagueName} = this.state;
        return (
            <div>
                <Header title="Add a league!" back="Back to Leagues" backLocation="/league" />
                <form className={"theLeagueForm"} onSubmit={this.submit}>
                    <TextField
                        id={"leagueName"}
                        label={"League Name"}
                        value={leagueName}
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

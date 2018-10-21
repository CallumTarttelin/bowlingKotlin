import Button from "@material-ui/core/Button/Button";
import Checkbox from "@material-ui/core/Checkbox/Checkbox";
import InputAdornment from "@material-ui/core/InputAdornment/InputAdornment";
import TextField from "@material-ui/core/TextField/TextField";
import axios from "axios";
import * as React from "react";
import {Component} from "react";
import ErrorMessage from "../other/ErrorMessage";

export interface AddScoreProps {
    playerGame: {id: number, player: {name: string}, handicap: number};
    refresh?: () => void;
}

export interface AddScoreState {
    id: number;
    errorMessage: string;
    error: object | null;
    scratch: number;
    handicap: number;
    checkHandicap: boolean;
}

export default class AddScore extends Component<AddScoreProps, AddScoreState> {
    constructor(props: AddScoreProps) {
        super(props);
        this.submit = this.submit.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.state = {
            id: props.playerGame.id,
            error: null,
            errorMessage: "",
            scratch: 0,
            handicap: props.playerGame.handicap,
            checkHandicap: false,
        };
    }

    public isValid() {
        if (this.state.scratch > 300) {
            return "Scratch cannot exceed 300!";
        } else if (this.state.scratch < 0) {
            return "Scratch cannot be negative";
        } else if (this.state.handicap > 80 && this.state.checkHandicap === true) {
            return "Handicap cannot exceed 80";
        } else if (this.state.handicap < 0 && this.state.checkHandicap === true) {
            return "Handicap cannot be negative";
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
        axios.post("/api/score", {
            playerGameId: this.state.id,
            scratch: this.state.scratch,
            handicap: this.state.checkHandicap ? this.state.handicap : null,
        })
            .then(() => {
                this.setState({scratch: 0});
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
    }

    public handleChange = (event: {target: {value: string, name: string}}) => {
        // @ts-ignore
        this.setState({[event.target.name]: event.target.value});
    }

    public handleHandicap = (event: {target: {checked: boolean}}) => {
        // @ts-ignore
        this.setState({checkHandicap: event.target.checked});
    }

    public render() {
        const {error, errorMessage, scratch, handicap, checkHandicap} = this.state;
        const name = this.props.playerGame.player.name;
        return (
            <td rowSpan={2} colSpan={2} className={this.props.playerGame.player.name + "-AddScreen"}>
                <form className={name + "-theScoreForm"} onSubmit={this.submit} noValidate={true}>

                    <TextField
                        id={name + "-scratch"}
                        name="scratch"
                        label="Scratch"
                        fullWidth={true}
                        value={scratch}
                        onChange={this.handleChange}
                        type="number"
                        className={"scratch"}
                        InputLabelProps={{
                            shrink: true,
                        }}
                        margin="normal"
                    />

                    <TextField
                        id={name + "-handicap"}
                        name="handicap"
                        label="Override handicap"
                        fullWidth={true}
                        value={handicap}
                        onChange={this.handleChange}
                        type="number"
                        className={"handicap"}
                        InputLabelProps={{
                            shrink: true,
                        }}
                        InputProps={{
                            startAdornment: (
                                <InputAdornment position="start">
                                    <Checkbox
                                        checked={checkHandicap}
                                        onChange={this.handleHandicap}
                                        name="checkHandicap"
                                        id={name + "-checkHandicap"}
                                        value="val"
                                        color="primary"
                                    />
                                </InputAdornment>
                            ),
                        }}
                        margin="normal"
                    />

                    <br/>

                    <Button
                        id={`${name}-submit'`}
                        type={"submit"}
                        variant={"raised"}
                        color={"primary"}
                        className={"submitForm"}
                    >
                        Submit
                    </Button>

                </form>
                <p className={"errorMessage"}>{errorMessage}</p>
                <ErrorMessage error={error}/>
            </td>
        );
    }
}

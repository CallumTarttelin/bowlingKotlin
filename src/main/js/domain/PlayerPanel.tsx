import {ExpansionPanel, ExpansionPanelDetails, ExpansionPanelSummary, Typography} from "@material-ui/core";
import CircularProgress from "@material-ui/core/CircularProgress/CircularProgress";
import {KeyboardArrowDown} from "@material-ui/icons";
import axios from "axios";
import * as React from "react";
import ErrorMessage from "../other/ErrorMessage";
import Player from "./Player";

export interface PlayerPanelProps {
    player: {name: string, id: number};
    changeTeam: (event: any, id: number) => void;
}

interface Player {
    id: string;
    name: string;
    team: {name: string, id: number};
    handicap: number;
    games: PlayerGame[];
    highGame: number;
    highSeries: number;
    lowGame: number;
    lowSeries: number;
}

interface PlayerGame {
    id: number;
    game: {id: number,
        venue: string,
        time: string,
        teams: Array<{name: string, id: number}>,
        winner: number,
    };
    scores: Array<{
        id: number,
        scratch: number,
        handicapped: number,
        score: number,
        total: boolean,
    }>;
}

export interface PlayerPanelState {
    status: string;
    id: number;
    player: Player;
    error: object | null;
}

export default class PlayerPanel extends React.Component<PlayerPanelProps, PlayerPanelState> {

    constructor(props: any) {
        super(props);
        this.state = {
            status: "Loading",
            id: props.player.id,
            player: {
                id: props.player.id,
                name: "Error not loaded",
                team: {name: "Error not loaded", id: -1},
                handicap: 0,
                games: [],
                highGame: 0,
                highSeries: 0,
                lowGame: 0,
                lowSeries: 0,
            },
            error: null,
        };
        this.getPlayer = this.getPlayer.bind(this);
    }

    public getPlayer() {
        if (this.state.status === "Loading") {
            axios.get("/api/player/" + this.state.id)
                .then((response) => {
                    this.setState({
                        status: "OK",
                        player: response.data,
                    });
                })
                .catch((error) => {
                    this.setState({status: "Error", error});
                });
        }
    }

    public render() {
        const {player, changeTeam} = this.props;
        return (
            <ExpansionPanel
                onClick={this.getPlayer}
                key={player.id}
                className={"player-" + player.name.replace(/\s+/g, "-").toLowerCase()}
            >
                <ExpansionPanelSummary expandIcon={<KeyboardArrowDown />}>
                    <Typography className={"Player"}>{player.name}</Typography>
                </ExpansionPanelSummary>
                <ExpansionPanelDetails>
                    {this.state.status === "OK" && <Player player={this.state.player} changeTeam={changeTeam}/>}
                    {this.state.status === "Loading" && <CircularProgress color={"primary"} />}
                    <ErrorMessage error={this.state.error}/>
                </ExpansionPanelDetails>
            </ExpansionPanel>
        );
    }
}

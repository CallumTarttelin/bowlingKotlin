import CircularProgress from "@material-ui/core/CircularProgress/CircularProgress";
import Button from "@material-ui/core/es/Button/Button";
import axios from "axios/index";
import * as React from "react";
import AddPlayers from "../add/AddPlayers";
import AddScore from "../add/AddScore";
import ErrorMessage from "../other/ErrorMessage";
import Header from "../other/Header";

// import AddScore from "../add/AddScore";

interface GameInterface {
    id: number;
    venue: string;
    time: string;
    league: {name: string, id: number};
    teams: TeamGame[];
}

interface TeamGame {
    id: number;
    name: string;
    players: Array<{player: {id: number, name: string}, id: number, scores: Score[], handicap: number}>;
    teamPlayer: {id: number, scores: Score[]} | null;
}

interface Score {
    id: number;
    scratch: number;
    handicapped: number;
    score: number;
    total: boolean;
}

interface GameProps {
    match: {params: {id: string}};
}

interface GameState {
    error: object | null;
    id: string;
    game?: GameInterface;
}

class Game extends React.Component<GameProps, GameState> {
    constructor(props: any) {
        super(props);
        this.state = {
            error: null,
            id: props.match.params.id,
        };
        this.getGame = this.getGame.bind(this);
        this.scoreGame = this.scoreGame.bind(this);
        this.getGame();
    }

    public getGame() {
        axios.get(`/api/game/${this.state.id}`)
            .then((response) => {
                this.setState({
                    game: response.data,
                });
            })
            .catch((error) => {
                this.setState({error});
            });
    }

    public scoreGame(event: any) {
        event.preventDefault();
        axios.post("/api/game/" + this.state.id)
            .then(() => {
                this.getGame();
            })
            .catch((error) => {
                if (error.response && error.response.status === 401) {
                    window.location.href = "/login";
                } else {
                    this.setState({error});
                }
            });
    }

    public render() {
        const {game, error} = this.state;
        if (game === undefined) {
            return (
                <React.Fragment>
                    <CircularProgress color={"primary"} />
                    <ErrorMessage error={error}/>
                </React.Fragment>
        );
        }

        const t0 = game.teams[0];
        const t0score = t0.teamPlayer !== null && game.teams.length >= 2 ?
            t0.teamPlayer.scores.reduce((b, scr) => b + scr.score, 0) +
            t0.players.reduce((a, plr) => a + plr.scores.reduce((b, scr) => b + scr.score, 0), 0) : undefined;
        const t1 = game.teams[1];
        const t1score = t1.teamPlayer !== null && game.teams.length >= 2 ?
            t1.teamPlayer.scores.reduce((b, scr) => b + scr.score, 0) +
            t1.players.reduce((a, plr) => a + plr.scores.reduce((b, scr) => b + scr.score, 0), 0) : undefined;

        return (
            <div className="Game">
                <Header
                    title={game.venue}
                    backLocation={`/league/${game.league.id}`}
                    back={game.league.name}
                    game={game}
                />
                {/*If both teams are empty*/}
                {
                    game.teams[0].players.length < 3 &&
                    <AddPlayers gameId={game.id} team={game.teams[0]}  refresh={this.getGame}/>
                }
                {/*If one team is empty*/}
                {
                    game.teams[0].players.length >= 3 && game.teams[1].players.length < 3 &&
                    <React.Fragment>
                      <GameTable team={game.teams[0]} refresh={this.getGame} />
                      <AddPlayers gameId={game.id} team={game.teams[1]} refresh={this.getGame}/>
                    </React.Fragment>
                }
                {/*If both teams are full*/}
                {
                    game.teams[0].players.length >= 3 && game.teams[1].players.length >= 3 &&
                    <React.Fragment>
                      <GameTable
                        team={game.teams[0]}
                        complete={true}
                        refresh={this.getGame}
                        points={t0score}
                        opposingPoints={t1score}
                      />
                      <br />
                      <GameTable
                        team={game.teams[1]}
                        complete={true}
                        refresh={this.getGame}
                        points={t1score}
                        opposingPoints={t0score}
                      />
                    </React.Fragment>
                }
                {/*If scores are also full*/}
                {
                    game.teams.every((teamGame) => teamGame.players.length === 3) &&
                    game.teams.every((teamGame) => teamGame.players.every((player) => player.scores.length === 3)) &&
                    game.teams.every((teamGame) => teamGame.teamPlayer === null) &&
                    <Button variant={"raised"} color={"primary"} id={"scoreGame"} onClick={this.scoreGame}>
                      Score Game
                    </Button>
                }
                <ErrorMessage error={this.state.error}/>
            </div>
        );
    }
}

interface GameTableProps {
    team: TeamGame;
    complete?: boolean;
    refresh: () => void;
    points?: number;
    opposingPoints?: number;
}

function GameTable({team, refresh, points, opposingPoints}: GameTableProps): JSX.Element {

    function fillRow(amount: number): JSX.Element[] {
        if (amount <= 0) { return [<React.Fragment key={"none"} />]; } else {
            const array = [];
            for (let x = 0; x < amount; x++) {
                array.push(
                    <React.Fragment key={x}>
                        <td />
                        <td />
                    </React.Fragment>,
                );
            }
            return array;
        }
    }

    function fillSecondRow(amount: number): JSX.Element[] {
        if (amount <= 0) { return [<React.Fragment key={"none"} />]; } else {
            const array = [];
            for (let x = 0; x < amount; x++) {
                array.push(<td colSpan={2} key={x} />);
            }
            return array;
        }
    }

    function isWinner(score: number | undefined, opposing: number | undefined): string {
        if (score === undefined || opposing === undefined) {
            return "";
        } else if (score > opposing) {
            return "winner";
        } else if (score < opposing) {
            return "loser";
        } else {
            return "tie";
        }
    }

    return (
        <table className={isWinner(points, opposingPoints)}>
            <tbody>
            <tr>
                <th colSpan={10}>{team.name}</th>
                <th>
                    {points !== undefined && opposingPoints !== undefined ?
                        points : ""
                    }
                </th>
            </tr>
            <tr>
                <th style={{ width: "6%" }}>HCP</th>
                <th style={{ width: "40%" }}>Bowler</th>
                <th style={{ width: "12%" }} colSpan={2}>Game 1</th>
                <th style={{ width: "12%" }} colSpan={2}>Game 2</th>
                <th style={{ width: "12%" }} colSpan={2}>Game 3</th>
                <th style={{ width: "12%" }} colSpan={2}>Total</th>
                <th style={{ width: "6%" }}>Pts</th>
            </tr>
            </tbody>
            {team.players.map((player, index) => (
                <tbody key={index}>
                    <tr>
                        <td rowSpan={2}>{player.handicap}</td>
                        <td rowSpan={2}>{player.player.name}</td>
                        {
                            player.scores.map((score, indx) => (
                                <React.Fragment key={indx}>
                                    <td>{score.scratch}</td>
                                    <td>{score.handicapped}</td>
                                </React.Fragment>
                            ))
                        }
                        {player.scores.length < 3 &&
                        <AddScore playerGame={player} refresh={refresh}/>}
                        {fillRow(player.scores.length === 3 ? 1 : 3 - player.scores.length)}
                        <td rowSpan={2}>{player.scores.reduce((acc, score) => acc + score.score, 0)}</td>
                    </tr>
                    <tr>
                        {
                            player.scores.map((score, indx) => (
                                <td colSpan={2} key={indx}>{score.score}</td>
                            ))
                        }
                        {fillSecondRow(player.scores.length === 3 ? 1 : 3 - player.scores.length)}
                    </tr>
                </tbody>
            ))}
            {team.teamPlayer !== null &&
                <tbody>
                <tr>
                  <td rowSpan={2} />
                  <td rowSpan={2} />
                  <td>{team.teamPlayer.scores[0].scratch}</td>
                  <td>{team.teamPlayer.scores[0].handicapped}</td>
                  <td>{team.teamPlayer.scores[1].scratch}</td>
                  <td>{team.teamPlayer.scores[1].handicapped}</td>
                  <td>{team.teamPlayer.scores[2].scratch}</td>
                  <td>{team.teamPlayer.scores[2].handicapped}</td>
                  <td>{team.teamPlayer.scores[3].scratch}</td>
                  <td>{team.teamPlayer.scores[3].handicapped}</td>
                  <td rowSpan={2}>{team.teamPlayer.scores.reduce((acc, score) => acc + score.score, 0)}</td>
                </tr>
                <tr>
                  <td colSpan={2}>{team.teamPlayer.scores[0].score}</td>
                  <td colSpan={2}>{team.teamPlayer.scores[1].score}</td>
                  <td colSpan={2}>{team.teamPlayer.scores[2].score}</td>
                  <td colSpan={2}>{team.teamPlayer.scores[3].score}</td>
                </tr>
                </tbody>
            }
        </table>
    );
}

export default Game;

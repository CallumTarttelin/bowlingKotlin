import * as React from "react";
import {Link} from "react-router-dom";

export interface PlayerGameSummaryProps {
    playerGame: PlayerGame;
    teamId: number;
    changeTeam: (event: any, id: number) => void;
}

interface PlayerGame {
    id: number;
    game: {
        id: number,
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

export default function PlayerGameSummary({playerGame, changeTeam, teamId}: PlayerGameSummaryProps): JSX.Element {
    const opposition = playerGame.game.teams.filter((a) => a.id !== teamId)[0];

    return (
        <tbody key={playerGame.id}>
        <tr>
            <td><Link to={"/game/" + playerGame.id}>
                {new Date(Date.parse(playerGame.game.time)).toLocaleString("en-GB", { timeZone: "UTC" })}
            </Link></td>
            <td>
                <Link
                    to={"/team/" + opposition.id}
                    onClick={(e) => changeTeam(e, opposition.id)}
                    id={opposition.id.toString()}
                >
                    {opposition.name}
                </Link>
            </td>
            {playerGame.scores.map((score: any) => (
                <React.Fragment key={score.id}>
                    <td className={"scratch"}>{score.scratch}</td>
                    <td className={"handicapped"}>{score.handicapped}</td>
                </React.Fragment>
            ))}
            <td>{playerGame.scores.reduce((a, b) => a + b.score, 0)}</td>
        </tr>
        </tbody>
    );
}

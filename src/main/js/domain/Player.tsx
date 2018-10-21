import * as React from "react";
import Delete from "../other/Delete";
import PlayerGameSummary from "../summary/PlayerGameSummary";

export interface PlayerProps {
    player: Player;
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

export default function Player({player, changeTeam}: PlayerProps): JSX.Element {
    const {id, name, team, handicap, games, highGame, highSeries, lowGame, lowSeries} = player;
    return (
        <div className={"Player"}>
            <table>
                <thead>
                <tr>
                    <th rowSpan={2} colSpan={4}><h2>{name}</h2></th>
                    <th colSpan={2}>Team</th>
                    <th>Handicap</th>
                    <th>High Game</th>
                    <th>High Series</th>
                    <th>Low Game</th>
                    <th>Low Series</th>
                    <th className={"invis"} />
                </tr>
                <tr>
                    <td colSpan={2}>
                        {team.name}
                        {/*<span className={"back"}><Link to={`/team/${team.id}}`}>{team.name}</Link></span>*/}
                    </td>
                    <td>{handicap}</td>
                    <td>{highGame}</td>
                    <td>{highSeries}</td>
                    <td>{lowGame}</td>
                    <td>{lowSeries}</td>
                    <td className={"invis"}><Delete id={id} type={"player"} name={name}/></td>
                </tr>
                <tr>
                    <th style={{width: "19%"}}>Date</th>
                    <th style={{width: "19%"}}>Opposition</th>
                    <th style={{width: "15%"}} colSpan={2}>Set 1</th>
                    <th style={{width: "15%"}} colSpan={2}>Set 2</th>
                    <th style={{width: "15%"}} colSpan={2}>Set 3</th>
                    <th style={{width: "15%"}} colSpan={2}>Total</th>
                    <th style={{width: "2%" }}>Points</th>
                </tr>
                </thead>
                {games.map((game: PlayerGame) => (
                    <PlayerGameSummary key={game.id} playerGame={game} teamId={team.id} changeTeam={changeTeam}/>
                ))}
            </table>
        </div>
    );
    // }
}

import * as React from "react";
import GameSummary from "../summary/GameSummary";

interface Game {
    id: number;
    venue: string;
    time: string;
    teams: Array<{name: string, id: number}>;
    winner: number;
}

export interface SortedGameListProps {
    games: Game[];
    changeTeamCallback?: (event: any, id: number) => void;
}

export default function SortedGameList({games, changeTeamCallback}: SortedGameListProps): JSX.Element {
    return (
        <div id={"Games"}>
            <h3>Games</h3>
            <ul className={"Games"}>
                {games.map((game: Game) => (
                    <GameSummary
                        key={game.id}
                        id={game.id}
                        winner={game.winner}
                        time={game.time}
                        teams={game.teams}
                        changeTeam={changeTeamCallback}
                    >
                        {game.venue}
                    </GameSummary>
                )).sort((a, b) => {
                        if (Number.isInteger(a.props.winner) && !Number.isInteger(b.props.winner)) {
                            return 1;
                        } else if (!Number.isInteger(a.props.winner) && Number.isInteger(b.props.winner)) {
                            return -1;
                        } else {
                            const aTime = Date.parse(a.props.time);
                            const bTime = Date.parse(b.props.time);
                            return aTime - bTime;
                        }
                    },
                )}
            </ul>
        </div>
    );
}

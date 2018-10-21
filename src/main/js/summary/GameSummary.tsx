import * as React from "react";
import {Link} from "react-router-dom";

export interface GameSummaryProps {
    teams: Array<{name: string, id: number}>;
    children: string;
    winner: number;
    id: number;
    time: string;
    changeTeam?: (event: any, id: number) => void;
}

export default function GameSummary({ teams, children, winner, id, time, changeTeam }: GameSummaryProps): JSX.Element {
    return (
        <li
            className={`${teams[0].name} vs ${teams[1].name} ${children !== "" ? "at-" + children : ""}`
                .replace(/\s+/g, "-").toLowerCase()}
        >
            {
                changeTeam === undefined ?
                    <React.Fragment>
                        <Link to={"/team/" + teams[0].id}>{teams[0].name}</Link>
                        {" vs "}
                        <Link to={"/team/" + teams[1].id}>{teams[1].name}</Link>
                    </React.Fragment> :
                    <React.Fragment>
                        <Link
                            onClick={(e) => changeTeam(e, teams[0].id)}
                            // className={"linkLike"}
                            to={"/team/" + teams[0].id}
                        >
                            {teams[0].name}
                        </Link>{" vs "}<Link
                            onClick={(e) => changeTeam(e, teams[1].id)}
                            // className={"linkLike"}
                            to={"/team/" + teams[1].id}
                        >
                        {teams[1].name}
                        </Link>
                    </React.Fragment>
            }

            {/*If not complete*/}
            {winner === -1 && <p>
                <Link to={"/game/" + id}>
                  {children} - {new Date(Date.parse(time)).toLocaleString("en-GB", { timeZone: "UTC" })}
                </Link>
            </p>}

            {/*If is complete and not a tie*/}
            {winner < 2 && winner > -1 && <p><Link to={"/game/" + id}>{teams[winner].name + " won!"}</Link></p>}

            {/*If it is a tie*/}
            {winner === 2 && <p><Link to={"/game/" + id}>Tie Game!</Link></p>}
        </li>
    );
}

import * as React from "react";
import {Link} from "react-router-dom";
import Delete from "../other/Delete";

export interface TeamSummaryProps {
    children: string;
    id: number;
    position: number;
    numGames: number;
    pinsFor: number;
    pinsAgainst: number;
    highHandicapGame: number;
    highHandicapSeries: number;
    teamPoints: number;
    totalPoints: number;
    leagueName: string;
}

export default function TeamSummary(props: TeamSummaryProps): JSX.Element {
    return (
        <tbody>
        <tr className={props.children.replace(/\s+/g, "-").toLowerCase()}>
            <td>{props.position}</td>
            <td>
                <Link to={{pathname: `/team/${props.id}`}}>
                    {props.children}
                </Link>
            </td>
            <td>{props.numGames}</td>
            <td>{props.pinsFor}</td>
            <td>{props.pinsAgainst}</td>
            <td>{props.highHandicapGame}</td>
            <td>{props.highHandicapSeries}</td>
            <td>{props.teamPoints}</td>
            <td>{props.totalPoints}</td>
            <td className={"invis"}><Delete id={props.id.toString()} type={"team"} name={props.children}/></td>
        </tr>
        </tbody>
    );
}

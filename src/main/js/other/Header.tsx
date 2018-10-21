import Button from "@material-ui/core/es/Button/Button";
import * as React from "react";
import {Link} from "react-router-dom";

export interface HeaderProps /*extends React.Props<HeaderProps>*/ {
    title: string;
    back?: string;
    backLocation?: string;
    game?: {teams: Array<{name: string, id: number}>, time: string} | null;
}

export default function Header({title, back = "", backLocation = "", game = null}: HeaderProps): JSX.Element {
    return (
        <header className="App-header">
            {backLocation !== "" && (
                <Link className="back" to={backLocation}>
                    <Button variant="raised">
                        {back}
                    </Button>
                </Link>
            )
            }
            {game === null && <h1 className="App-title">
                {title}
            </h1>}
            {game !== null && <React.Fragment>
                <h1 className="App-title-game">
                    <Link
                        to={"/team/" + game.teams[0].id}
                    >
                        {game.teams[0].name}
                    </Link> vs <Link
                        to={"/team/" + game.teams[1].id}
                    >
                    {game.teams[1].name}
                    </Link>
                </h1>
                <h3>
                    {new Date(Date.parse(game.time)).toLocaleString("en-GB", { timeZone: "UTC" })} at {title}
                </h3>
            </React.Fragment>
            }
        </header>
    );
}

import Button from "@material-ui/core/es/Button/Button";
import CircularProgress from "@material-ui/core/es/CircularProgress/CircularProgress";
import AddIcon from "@material-ui/icons/Add";
import axios from "axios";
import * as React from "react";
import {Component} from "react";
import {Link} from "react-router-dom";
import LeagueSummary from "../summary/LeagueSummary";
import ErrorMessage from "./ErrorMessage";
import Header from "./Header";

interface LeagueListState {
    status: string;
    error: object;
    leagues: object[];
}

class LeagueList extends Component<{}, LeagueListState> {
    public readonly state = {error: {}, leagues: [{name: "", id: 0}], status: "Loading"};
    constructor(props: any) {
        super(props);
        this.update = this.update.bind(this);
        this.update();
    }

    public update() {
        axios.get("/api/league")
            .then((response) => {
                this.setState({ status: "ok", leagues: response.data });
            })
            .catch((err) => {
                this.setState({ status: "Error", error: err });
            });
    }

    public render() {
        const { error, leagues, status } = this.state;
        switch (status) {
            case "Loading": return (
                <div className="Loading">
                    <Header title={"Bowling Leagues!"}/>
                    <CircularProgress color="primary" />
                </div>
            );
            case "Error":
                return (
                <div className="Error">
                    <Header title={"Error!"}/>
                    <ErrorMessage error={error}/>
                    <Button variant="raised" color="primary" className="RefreshButton" onClick={this.update}>
                        Refresh Leagues
                    </Button>
                </div>
            );

            default: return (
                <div className="LeagueList">
                    <Header title="Bowling Leagues!" />
                    <div className="Leagues">
                        <ul>
                            {leagues.map((league) => (
                                <LeagueSummary id={league.id} key={league.id}>
                                    {league.name}
                                </LeagueSummary>
                            ))}
                        </ul>
                        <Button variant="raised" color="primary" className="RefreshButton" onClick={this.update}>
                            Refresh Leagues
                        </Button>
                        <Link to="/add">
                            <Button variant="fab" color="primary" className="addButton" ><AddIcon /></Button>
                        </Link>
                    </div>
                </div>
            );
        }
    }
}

export default LeagueList;

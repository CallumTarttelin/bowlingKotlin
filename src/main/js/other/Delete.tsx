import {IconButton} from "@material-ui/core";
import DeleteIcon from "@material-ui/icons/Delete";
import axios from "axios";
import * as React from "react";
import ErrorMessage from "./ErrorMessage";

export interface DeleteProps {
    id: string;
    type: string;
    name: string;
}

export interface DeleteState {
    id: string;
    type: string;
    error: object | null;
}

export default class Delete extends React.Component<DeleteProps, DeleteState> {
    constructor(props: DeleteProps) {
        super(props);
        this.state = ({id: props.id, type: props.type, error: null});
        this.deleteThing = this.deleteThing.bind(this);
    }

    public deleteThing() {
        axios.delete("/api/" + this.state.type + "/" + this.state.id)
            .then(() => {
                location.reload();
            })
            .catch((error) => {
                if (error.response.status === 401) {
                    window.location.href = "/login";
                } else {
                    this.setState({error});
                }
            });
    }

    public render() {
        return (
            <React.Fragment>
                <IconButton
                    id={"delete-" + this.props.id.toString()}
                    onClick={this.deleteThing}
                    aria-label={"Delete"}
                    name={"delete-" + this.props.name.replace(/\s+/g, "-").toLowerCase()}
                >
                    <DeleteIcon/>
                </IconButton>
                <ErrorMessage error={this.state.error}/>
            </React.Fragment>
        );
    }
}

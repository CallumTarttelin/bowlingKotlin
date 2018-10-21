import * as React from "react";
import {Link} from "react-router-dom";
import Delete from "../other/Delete";

export interface LeagueSummaryProps {
    children: string;
    id: number;
}

export default function LeagueSummary({ children, id }: LeagueSummaryProps): JSX.Element {
  return (
    <li className={children.replace(/\s+/g, "-").toLowerCase()}>
      <Link to={`/league/${id.toString()}`}>
          {children}
      </Link>
      <Delete id={id.toString()} name={children} type={"league"} />
    </li>
  );
}

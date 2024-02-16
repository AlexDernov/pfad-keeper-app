import {MyCoords} from "./MyCoords.tsx";

export type MyRoute = {
    id: string;
    coords: MyCoords[];
    name: string;
    dateTime: Date
}
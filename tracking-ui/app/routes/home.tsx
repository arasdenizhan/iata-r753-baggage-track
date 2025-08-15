import LuggageTracker from "~/luggage/luggagetracker";
import type { Route } from "./+types/home";

export function meta({}: Route.MetaArgs) {
  return [
    { title: "Luggage Tracker" },
    { name: "description", content: "Welcome to Luggage Tracking App!" },
  ];
}

export default function Home() {
  return <LuggageTracker />;
}

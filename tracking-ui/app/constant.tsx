export const airportsMap: Map<string, Airport> = new Map([
  ["IST", { name: "Istanbul Airport", position: [41.2753, 28.7519] }],
  ["SAW", { name: "Sabiha Gokcen Airport", position: [40.8986, 29.3094] }],
  ["ESB", { name: "Ankara Esenboga Airport", position: [40.1281, 32.9958] }],
  [
    "ADB",
    { name: "Izmir Adnan Menderes Airport", position: [38.2923, 27.1567] },
  ],
  ["AYT", { name: "Antalya Airport", position: [36.8987, 30.8] }],
  ["DLM", { name: "Dalaman Airport", position: [36.7133, 28.7922] }],
  ["BJV", { name: "Bodrum Milas Airport", position: [37.2506, 27.6642] }],
]);

export interface Airport {
  name: string;
  position: [number, number];
}

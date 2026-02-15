package com.example.constructiondelivery;

import java.util.ArrayList;
import java.util.List;

public class DataHelper {

    private static String userAddress = "A-123, Rosewood Apartments, MG Road, Pune, Maharashtra - 411001";

    public static String getUserAddress() {
        return userAddress;
    }

    public static void setUserAddress(String address) {
        userAddress = address;
    }

    public static List<Material> getAllMaterials() {
        List<Material> allMaterials = new ArrayList<>();
        int img = R.drawable.landing_image;

        // 1. Natural Materials
        allMaterials.add(new Material("River Sand", "Natural Materials", "₹1,200/ton", "1 Ton", "Local Quarry", "Fine sand for plastering", "Grade-A, Triple washed", "Sourced from local river beds, ideal for fine finishing work.", img));
        allMaterials.add(new Material("Crushed Stone (20mm)", "Natural Materials", "₹950/ton", "1 Ton", "Aggregates Ltd", "Aggregate for concrete", "Standard building grade", "Machine crushed basalt rocks for RCC work.", img));
        allMaterials.add(new Material("Red Earth", "Natural Materials", "₹400/truck", "1 Truck", "Earth Movers", "For backfilling", "High compaction", "Used for leveling sites and garden base.", img));
        allMaterials.add(new Material("Gravel", "Natural Materials", "₹800/ton", "1 Ton", "Local Supplier", "For drainage and base", "Unprocessed", "Mixed size natural pebbles.", img));
        allMaterials.add(new Material("Black Cotton Soil", "Natural Materials", "₹300/truck", "1 Truck", "Farm Supply", "Agricultural use", "Fertile", "Rich organic soil for landscaping.", img));
        allMaterials.add(new Material("Boulders", "Natural Materials", "₹1,500/ton", "1 Ton", "Stone Works", "Retaining walls", "Hard Rock", "Large stones for heavy foundation and walls.", img));
        allMaterials.add(new Material("Yellow Soil", "Natural Materials", "₹500/truck", "1 Truck", "Local Supply", "Plinth filling", "Sandy Loam", "Low shrinkage soil for filling.", img));

        // 2. Cement & Binding Materials
        allMaterials.add(new Material("OPC 53 Grade", "Cement & Binding Materials", "₹380/bag", "50 Kg", "UltraTech", "High strength concrete", "IS 12269", "Best for multi-story RCC structures.", img));
        allMaterials.add(new Material("PPC Cement", "Cement & Binding Materials", "₹360/bag", "50 Kg", "Ambuja", "Masonry and plastering", "IS 1489", "Lower heat of hydration, eco-friendly.", img));
        allMaterials.add(new Material("White Cement", "Cement & Binding Materials", "₹900/bag", "25 Kg", "Birla White", "Decorative work", "Superior whiteness", "Used for grouting and wall putty base.", img));
        allMaterials.add(new Material("Hydrated Lime", "Cement & Binding Materials", "₹200/bag", "20 Kg", "Lime Corp", "Traditional mortar", "90% purity", "Improves workability and water retention.", img));
        allMaterials.add(new Material("Gypsum Plaster", "Cement & Binding Materials", "₹450/bag", "25 Kg", "Gyproc", "Internal wall finish", "Lightweight", "Replacement for sand-cement plaster.", img));
        allMaterials.add(new Material("Wall Putty", "Cement & Binding Materials", "₹850/40kg", "40 Kg", "JK Wallmaxx", "Smooth wall finish", "Water resistant", "White cement-based putty for paints.", img));
        allMaterials.add(new Material("Tile Adhesive", "Cement & Binding Materials", "₹650/bag", "20 Kg", "Roff", "Fixing tiles", "Polymer modified", "For floor and wall tile application.", img));

        // 3. Bricks & Blocks
        allMaterials.add(new Material("Fly Ash Bricks", "Bricks & Blocks", "₹7/piece", "1 Piece", "Green Build", "Masonry walls", "Class 7.5", "Eco-friendly, uniform shape bricks.", img));
        allMaterials.add(new Material("Red Clay Bricks", "Bricks & Blocks", "₹9/piece", "1 Piece", "Local Kiln", "Standard construction", "First Class", "Hand-molded sun-dried and kiln-burnt.", img));
        allMaterials.add(new Material("AAC Blocks", "Bricks & Blocks", "₹3,500/cbm", "1 CBM", "Siporex", "Lightweight walls", "Grade 1", "Thermal insulation and fire resistant.", img));
        allMaterials.add(new Material("Concrete Blocks (Solid)", "Bricks & Blocks", "₹45/piece", "1 Piece", "Block Tech", "Load bearing walls", "High density", "Used for compound walls and basement.", img));
        allMaterials.add(new Material("Hollow Concrete Blocks", "Bricks & Blocks", "₹40/piece", "1 Piece", "Block Tech", "Partition walls", "Standard size", "Lightweight and easy to handle.", img));
        allMaterials.add(new Material("Laterite Stones", "Bricks & Blocks", "₹60/piece", "1 Piece", "Coastal Quarry", "Rustic walling", "Natural stone", "Traditional building blocks used in coastal India.", img));
        allMaterials.add(new Material("Interlocking Pavers", "Bricks & Blocks", "₹25/sqft", "1 Sqft", "Pave Masters", "Driveway/walkway", "60mm thickness", "Zig-zag pattern high strength blocks.", img));

        // 4. Structural Materials
        allMaterials.add(new Material("TMT Steel Bars (12mm)", "Structural Materials", "₹65,000/ton", "1 Ton", "Tata Tiscon", "RCC reinforcement", "Fe 550D", "Ductile steel bars for seismic zones.", img));
        allMaterials.add(new Material("Binding Wire", "Structural Materials", "₹80/kg", "1 Kg", "Local Steel", "Steel fixing", "18 Guage", "GI wire for tying reinforcement bars.", img));
        allMaterials.add(new Material("MS Angles", "Structural Materials", "₹60/kg", "1 Kg", "JSW", "Steel fabrication", "Standard L-shape", "Used for trusses and frame structures.", img));
        allMaterials.add(new Material("I-Beams", "Structural Materials", "₹70/kg", "1 Kg", "SAIL", "Heavy structural support", "ISMB 200", "Hot rolled structural steel beams.", img));
        allMaterials.add(new Material("GI Pipes", "Structural Materials", "₹1,200/length", "6 Meter", "Apollo", "Structure scaffolding", "Class B", "Galvanized iron pipes for frames.", img));
        allMaterials.add(new Material("Chequered Plates", "Structural Materials", "₹65/kg", "1 Kg", "Steel Corp", "Flooring/Stairs", "Anti-skid", "Heavy duty steel plates with patterns.", img));
        allMaterials.add(new Material("Welded Wire Mesh", "Structural Materials", "₹120/sqm", "1 Sqm", "Mesh Tech", "Slab reinforcement", "BRC fabric", "Pre-welded mesh for quick installation.", img));

        // 5. Finishing Materials
        allMaterials.add(new Material("Vitrified Floor Tiles", "Finishing Materials", "₹45/sqft", "1 Sqft", "Kajaria", "Flooring", "600x600mm, Nano finish", "Double charged high durability tiles.", img));
        allMaterials.add(new Material("Emulsion Paint", "Finishing Materials", "₹280/liter", "1 Liter", "Asian Paints", "Interior wall painting", "Royale Luxury", "Washable, high sheen finish.", img));
        allMaterials.add(new Material("Granite Slab", "Finishing Materials", "₹180/sqft", "1 Sqft", "Rajasthan Stone", "Kitchen platform", "Black Galaxy", "Polished natural stone for counters.", img));
        allMaterials.add(new Material("Wooden Laminate", "Finishing Materials", "₹3,500/sheet", "8x4 Ft", "Greenlam", "Furniture finish", "1.0mm thickness", "Decorative laminate for wardrobes.", img));
        allMaterials.add(new Material("False Ceiling (POP)", "Finishing Materials", "₹90/sqft", "1 Sqft", "Local Pro", "Interior ceiling", "Designer finish", "Plaster of Paris decorative ceiling.", img));
        allMaterials.add(new Material("Aluminum Window Frame", "Finishing Materials", "₹450/sqft", "1 Sqft", "Jindal", "Window glazing", "Powder coated", "Sliding window frames with mesh.", img));
        allMaterials.add(new Material("Door Frame (Teak Wood)", "Finishing Materials", "₹12,000/unit", "1 Unit", "Timber Mart", "Door entry", "CP Teak", "Natural seasoned teak wood frame.", img));

        // 6. Roofing Materials
        allMaterials.add(new Material("Mangalore Roof Tiles", "Roofing Materials", "₹25/piece", "1 Piece", "Coastal Tiles", "Traditional roofing", "Terra Cotta", "Natural clay tiles for slope roofs.", img));
        allMaterials.add(new Material("GI Roofing Sheets", "Roofing Materials", "₹450/sqm", "1 Sqm", "Tata BlueScope", "Industrial roofing", "Pre-painted", "Durable color coated steel sheets.", img));
        allMaterials.add(new Material("Polycarbonate Sheets", "Roofing Materials", "₹180/sqft", "1 Sqft", "Lexan", "Skylights", "UV Protected", "Transparent roofing for natural light.", img));
        allMaterials.add(new Material("Asphalt Shingles", "Roofing Materials", "₹120/sqft", "1 Sqft", "CertainTeed", "Modern roofing", "Multi-layered", "Fiberglass reinforced bitumen shingles.", img));
        allMaterials.add(new Material("Fiber Cement Sheets", "Roofing Materials", "₹40/sqft", "1 Sqft", "Everest", "Shed roofing", "Corrugated", "Non-asbestos cement roofing sheets.", img));
        allMaterials.add(new Material("UPVC Roofing", "Roofing Materials", "₹150/sqft", "1 Sqft", "Roof King", "Domestic sheds", "Heat resistant", "Multi-layer plastic roofing panels.", img));
        allMaterials.add(new Material("Thatch Panels", "Roofing Materials", "₹200/sqm", "1 Sqm", "Nature Build", "Eco-resorts", "Synthetic Palm", "Artificial thatch for tropical look.", img));

        // 7. Plumbing & Sanitary Materials
        allMaterials.add(new Material("CPVC Pipes (1 inch)", "Plumbing & Sanitary Materials", "₹450/length", "3 Meter", "Ashirvad", "Hot/Cold water supply", "SDR 11", "Chlorinated PVC pipes for plumbing.", img));
        allMaterials.add(new Material("SWR PVC Pipes", "Plumbing & Sanitary Materials", "₹850/length", "3 Meter", "Supreme", "Drainage system", "Ring fit", "Soil, waste and rain water pipes.", img));
        allMaterials.add(new Material("Ceramic Water Closet", "Plumbing & Sanitary Materials", "₹4,500/unit", "1 Unit", "Hindware", "Bathroom fixture", "European Style", "White ceramic floor mounted closet.", img));
        allMaterials.add(new Material("Kitchen Sink", "Plumbing & Sanitary Materials", "₹3,200/unit", "1 Unit", "Neelkanth", "Utility sink", "Stainless Steel 304", "Single bowl satin finish sink.", img));
        allMaterials.add(new Material("Brass Bib Tap", "Plumbing & Sanitary Materials", "₹650/piece", "1 Piece", "Jaquar", "Water outlet", "Chrome plated", "High quality quarter turn bib cock.", img));
        allMaterials.add(new Material("Overhead Water Tank", "Plumbing & Sanitary Materials", "₹6/liter", "1000 Ltr", "Sintex", "Water storage", "3-Layer LLDPE", "Food grade plastic water tank.", img));
        allMaterials.add(new Material("Floor Trap", "Plumbing & Sanitary Materials", "₹120/piece", "1 Piece", "Prince", "Drainage trap", "P-Trap design", "Prevents foul smell from drains.", img));

        // 8. Electrical Materials
        allMaterials.add(new Material("Copper Wire (1.5 sqmm)", "Electrical Materials", "₹1,450/coil", "90 Meter", "Polycab", "House wiring", "FR - Flame Retardant", "Pure copper multi-strand wire coil.", img));
        allMaterials.add(new Material("MCB (16A Single Pole)", "Electrical Materials", "₹220/piece", "1 Piece", "Havells", "Circuit protection", "Type C Curve", "Prevents overload and short circuit.", img));
        allMaterials.add(new Material("PVC Conduit Pipe", "Electrical Materials", "₹80/length", "3 Meter", "Precision", "Wire casing", "Medium Duty", "Non-flammable PVC pipes for wiring.", img));
        allMaterials.add(new Material("Modular Switch", "Electrical Materials", "₹45/piece", "1 Piece", "Anchor Roma", "Electrical control", "6A Switch", "Soft touch modern modular switch.", img));
        allMaterials.add(new Material("LED Downlight", "Electrical Materials", "₹350/unit", "1 Unit", "Philips", "Ambient lighting", "12W, Warm White", "Recessed ceiling LED panel light.", img));
        allMaterials.add(new Material("Distribution Board", "Electrical Materials", "₹1,800/unit", "1 Unit", "Legrand", "Power distribution", "8-Way TPN", "Enclosure for MCBs and RCDs.", img));
        allMaterials.add(new Material("GI Earthing Plate", "Electrical Materials", "₹1,200/set", "1 Unit", "Local Electrical", "Safety earthing", "600x600mm", "Galvanized iron plate for grounding.", img));

        // 9. Insulation & Waterproofing
        allMaterials.add(new Material("Liquid Waterproofing", "Insulation & Waterproofing", "₹2,500/can", "20 Ltr", "Dr. Fixit", "Roof waterproofing", "LW+ Compound", "SBR latex for concrete waterproofing.", img));
        allMaterials.add(new Material("Bitumen Membrane", "Insulation & Waterproofing", "₹180/sqm", "10 Sqm", "Fosroc", "Basement tanking", "App modified", "Torch-applied waterproofing sheet.", img));
        allMaterials.add(new Material("Glass Wool", "Insulation & Waterproofing", "₹85/sqm", "20 Sqm", "UP Twiga", "Thermal insulation", "Fiberglass", "Rolls for attic and wall insulation.", img));
        allMaterials.add(new Material("PU Foam Spray", "Insulation & Waterproofing", "₹650/can", "750 ml", "Abro", "Gap filling", "Expanding foam", "Seals cracks and provides insulation.", img));
        allMaterials.add(new Material("Expanded Polystyrene", "Insulation & Waterproofing", "₹450/sheet", "1 Sheet", "Thermocol Ltd", "Cold storage", "High density", "EPS sheets for floor insulation.", img));
        allMaterials.add(new Material("Aluminum Foil Tape", "Insulation & Waterproofing", "₹350/roll", "50 Meter", "3M", "Duct sealing", "Heat reflective", "For sealing AC ducts and insulation.", img));
        allMaterials.add(new Material("Crystalline Coating", "Insulation & Waterproofing", "₹3,800/bag", "25 Kg", "Xypex", "Internal waterproofing", "Reactive chemical", "Deep penetrating concrete sealer.", img));

        return allMaterials;
    }
}

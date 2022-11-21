package com.example.weightliftingapp

internal object ExpandableListData {
    val data: HashMap<String, List<String>>
        get() {
            val expandableListDetail =
                HashMap<String, List<String>>()
            val chest: MutableList<String> = ArrayList()
            chest.add("Bench Press")
            chest.add("Push Up")
            chest.add("Dumbbell Fly")
            chest.add("Barbell/ Dumbbell Pullover")
            chest.add("Parallel Bar Dip")
            chest.add("Cable Crossover")
            chest.add("Incline Bench Press")
            val shoulder: MutableList<String> = ArrayList()
            shoulder.add("Shoulder Press")
            shoulder.add("Lateral Dumbbell Raise")
            shoulder.add("Bent-Over Lateral Raise")
            shoulder.add("Alternate Front Arm Raise")
            shoulder.add("Low-Pulley Front Arm Raise")
            shoulder.add("Upright Row")
            shoulder.add("Arnold Press")
            val bicep: MutableList<String> = ArrayList()
            bicep.add("Concentration Curls")
            bicep.add("Dumbbell Curls")
            bicep.add("Barbell Curls")
            bicep.add("Reverse Barbell Curls")
            bicep.add("High-pulley Curls")
            bicep.add("Low-pulley Curls")
            bicep.add("Preacher Curls")
            bicep.add("Hammer Curls")
            val tricep: MutableList<String> = ArrayList()
            tricep.add("Bar Dip")
            tricep.add("Lying Tricep Extension")
            tricep.add("Close Grip Bench Press")
            tricep.add("Weighted Bench Dip")
            tricep.add("Dip Machine")
            tricep.add("Skullcrusher")
            tricep.add("Rope Cable Overhead")
            tricep.add("Push Down Cable")
            tricep.add("Push up w/ Close Grip")
            tricep.add("Cable Kick Back")
            val back: MutableList<String> = ArrayList()
            back.add("Deadlift")
            back.add("Chin Ups")
            back.add("Pull-Ups")
            back.add("Lat Pull-Downs")
            back.add("Seated Cable Rows")
            back.add("Dumbbell Rows")
            back.add("Barbell Rows")
            back.add("Back Extensions")
            back.add("Rear Delt Fly")
            val hamstring: MutableList<String> = ArrayList()
            hamstring.add("Prone Leg-Curl Machine")
            hamstring.add("Romanian Deadlift")
            hamstring.add("Seated Leg Curl")
            hamstring.add("Bulgarian Split Squat")
            val thighs: MutableList<String> = ArrayList()
            thighs.add("Back Squat")
            thighs.add("Leg Press")
            thighs.add("Leg Curls")
            thighs.add("Leg Extensions")
            thighs.add("Dumbbell Lunges")
            thighs.add("Dumbbell Deadlift")
            thighs.add("Straight Leg Deadlift")
            val glutes: MutableList<String> = ArrayList()
            glutes.add("Cable Hip Extension")
            glutes.add("Leg Press")
            glutes.add("Deadlift")
            glutes.add("Bulgarian Split Squat")
            glutes.add("Hip Extension")
            glutes.add("Stiff Leg Deadlift")
            glutes.add("Barbell Hip Thrust")
            val calves: MutableList<String> = ArrayList()
            calves.add("Standing Barbell Calf Raise")
            calves.add("One-Legged Dumbbell Calf Raise")
            calves.add("Resistance Band Calf Extension")
            calves.add("Seated Dumbell Calf Raise")
            calves.add("Standing Resistance Band Calf Raise")
            calves.add("Calf Machine")
            val abs: MutableList<String> = ArrayList()
            abs.add("Seated Ab Crunch")
            abs.add("Dumbbell Side Bends")
            abs.add("Truck Crunch")
            abs.add("Decline Leg Raises")
            abs.add("Decline Crunch")
            abs.add("Weighted Cable Crunch")
            abs.add("Russian Twists")

            expandableListDetail["CHEST"] = chest
            expandableListDetail["SHOULDERS"] = shoulder
            expandableListDetail["BICEP"] = bicep
            expandableListDetail["TRICEP"] = tricep
            expandableListDetail["BACK"] = back
            expandableListDetail["HAMSTRING"] = hamstring
            expandableListDetail["THIGHS"] = thighs
            expandableListDetail["GLUTES"] = glutes
            expandableListDetail["CALVES"] = calves
            expandableListDetail["ABS"] = abs
            return expandableListDetail
        }
}
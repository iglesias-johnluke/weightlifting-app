package com.example.weightliftingapp

class Exercise(name: String, sets: Int, reps: Int, weight: Int, muscleGroup: MuscleGroup) {

    private var name  = name
    private var sets = sets
    private var reps = reps
    private var weight = weight
    private var muscleGroup = muscleGroup


    fun getName():String{
        return name
    }
    fun setName(newName:String){
        name = newName
    }
    fun getSets():Int{
        return sets
    }
    fun setSets(numSets:Int){
        sets = numSets

    }
    fun getReps():Int{
        return reps
    }
    fun setReps(numReps:Int){
        reps = numReps
    }
    fun getWeight():Int{
        return weight
    }
    fun setWeight(numLbs:Int){
        weight = numLbs

    }
    fun getMuscleGroup():MuscleGroup{
        return muscleGroup
    }
    fun setMuscleGroup(newGroup:MuscleGroup){
        muscleGroup = newGroup
    }

    override fun toString(): String {
        return "Exercise(name='$name', sets=$sets, reps=$reps, weight=$weight, muscleGroup=$muscleGroup)"
    }


}
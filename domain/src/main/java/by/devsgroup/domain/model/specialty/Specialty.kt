package by.devsgroup.domain.model.specialty

data class Specialty(
    val id: Long,
    val name: String,
    val abbrev: String,
    val facultyId: Long,
    val code: String,
    val educationForm: SpecialtyEducationForm,
)



package so.pillow.sdk

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PillowStudyTest {
  @Test
  fun exposesStudyId() {
    val study = PillowStudy(id = "demo")

    assertEquals("demo", study.id)
  }

  @Test
  fun exposesStudyListener() {
    val listener: PillowStudyListener = object : PillowStudyListener {}

    assertNotNull(listener)
  }
}

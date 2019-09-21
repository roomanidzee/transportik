package com.romanidze.transportik.config

import pureconfig.ConfigSource
import pureconfig.error.ConfigReaderFailures
import pureconfig.generic.auto._

object ConfigurationLoader {

  def load: Either[ConfigReaderFailures, ApplicationConfig] =
    ConfigSource.default.load[ApplicationConfig]

}
